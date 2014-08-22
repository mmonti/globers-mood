package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.events.DispatchCampaignSuccessEvent;
import com.globant.labs.mood.events.DispatchUserSuccessEvent;
import com.globant.labs.mood.events.PendingDispatchEvent;
import com.globant.labs.mood.exception.BusinessException;
import com.globant.labs.mood.model.mail.*;
import com.globant.labs.mood.model.persistent.Attachment;
import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.Template;
import com.globant.labs.mood.model.persistent.User;
import com.globant.labs.mood.repository.data.CampaignRepository;
import com.globant.labs.mood.repository.data.UserRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.MailingService;
import com.globant.labs.mood.service.mail.MailMessageFactory;
import com.google.appengine.api.datastore.Blob;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import static com.globant.labs.mood.exception.BusinessException.ErrorCode.RESOURCE_NOT_FOUND;
import static com.globant.labs.mood.support.StringSupport.on;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Service
public class MailingServiceImpl extends AbstractService implements MailingService {

    private static final Logger logger = LoggerFactory.getLogger(MailingServiceImpl.class);

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_ID = "Content-ID";
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html";

    @Inject
    private Session session;

    @Inject
    private MailMessageFactory mailMessageFactory;

    @Inject
    private CampaignRepository campaignRepository;

    @Inject
    private UserRepository userRepository;

    @Override
    public void dispatch(final Long campaignId) {
        Preconditions.checkNotNull(campaignId, "campaignId is null");

        logger.info("method=dispatch(), args=[campaignId=[{}]]", campaignId);

        final Campaign campaign = campaignRepository.findOne(campaignId);
        if (campaign == null) {
            logger.error("method=dispatch() - campaignId=[{}] not found", campaignId);
            throw new BusinessException(on("Campaign with campaignId=[{}] not found.", campaignId), RESOURCE_NOT_FOUND);
        }

        logger.info("method=dispatch() - generating emails to dispatch campaignId=[{}]", campaignId);
        final Set<MailMessage> messages = mailMessageFactory.create(campaign);

        final DispatchResult dispatchResult = new CampaignDispatchResult(campaign);
        logger.info("method=dispatch() - dispatching campaignId=[{}]", campaignId);
        dispatch(dispatchResult, messages);

        // = Handle dispatching results.
        handleDispatchingResult(dispatchResult);
    }

    /**
     * @param campaignId
     * @param userId
     */
    @Override
    public void dispatch(final Long campaignId, final Long userId) {
        Preconditions.checkNotNull(campaignId, "campaignId is null");
        Preconditions.checkNotNull(userId, "userId is null");

        logger.info("method=dispatch(), args=[campaignId=[{}, userId=[{}]]", campaignId, userId);

        final Campaign campaign = campaignRepository.findOne(campaignId);
        if (campaign == null) {
            logger.error("method=dispatch() - campaignId=[{}] not found", campaignId);
            throw new BusinessException(on("Campaign with campaignId=[{}] not found.", campaignId), RESOURCE_NOT_FOUND);
        }

        final User user = userRepository.findOne(userId);
        if (user == null) {
            logger.error("method=dispatch() - userId=[{}] not found", userId);
            throw new BusinessException(on("User with userId=[{}] not found.", userId), RESOURCE_NOT_FOUND);
        }

        logger.info("method=dispatch() - generating email to dispatch to userId=[{}] of campaignId=[{}]", userId, campaignId);
        final MailMessage message = mailMessageFactory.create(campaign, user);

        logger.info("method=dispatch() - dispatching to userId=[{}]", userId);
        final DispatchResult dispatchResult = new UserDispatchResult(campaign, user);
        dispatch(dispatchResult, message);

        // = Handle dispatching results.
        handleDispatchingResult(dispatchResult);
    }

    /**
     * @param dispatchResult
     */
    private void handleDispatchingResult(final DispatchResult dispatchResult) {
        Preconditions.checkNotNull(dispatchResult, "dispatchResult is null");

        logger.info("method=handleDispatchingResult(), args=[dispatchResult=[{}]]", dispatchResult);

        if (dispatchResult.hasPendings() & !dispatchResult.hasDispatched()) {
            logger.info("method=handleDispatchingResult() - found [{}] emails pending to dispatch", dispatchResult.getDispatchPending().size());
            publish(new PendingDispatchEvent(this, dispatchResult));

        } else {
            final Campaign campaign = dispatchResult.getCampaign();
            if (dispatchResult instanceof CampaignDispatchResult) {
                logger.info("method=handleDispatchingResult() - dispatched [{}] emails of campaignId=[{}] successfully", dispatchResult.getDispatched().size(), campaign.getId());
                publish(new DispatchCampaignSuccessEvent(this, campaign));

            } else {
                final User user = UserDispatchResult.class.cast(dispatchResult).getUser();
                publish(new DispatchUserSuccessEvent(this, campaign, user));
            }
        }
    }

    /**
     * @param dispatchResult
     * @param mailMessages
     */
    private void dispatch(final DispatchResult dispatchResult, final Set<MailMessage> mailMessages) {
        Preconditions.checkNotNull(dispatchResult, "dispatchResult is null");
        Preconditions.checkNotNull(mailMessages, "mailMessages is null");

        logger.info("method=dispatch(), args=[dispatchResult=[{}], mailMessages=[{}]]", dispatchResult, mailMessages);

        for (final MailMessage currentMailMessage : mailMessages) {
            dispatch(dispatchResult, currentMailMessage);
        }
    }

    /**
     * @param dispatchResult
     * @param mailMessage
     */
    private void dispatch(final DispatchResult dispatchResult, final MailMessage mailMessage) {
        Preconditions.checkNotNull(dispatchResult, "dispatchResult is null");
        Preconditions.checkNotNull(mailMessage, "mailMessage is null");

        logger.info("method=dispatch(), args=[dispatchResult=[{}], mailMessage=[{}]]", dispatchResult, mailMessage);

        final Message message = getMessage(mailMessage);
        if (message != null) {
            try {
                logger.info("method=dispatch() - sending email message=[{}]", message);
                Transport.send(message);
                dispatchResult.addAsDispatched(mailMessage);

            } catch (MessagingException e) {
                logger.info("method=dispatch() - error sending email message=[{}]", message);
                dispatchResult.addAsPendingToDispatch(mailMessage);
            }
        } else {
            logger.info("method=dispatch() - message null, adding mailMessage=[{}] to pending list", mailMessage);
            dispatchResult.addAsPendingToDispatch(mailMessage);
        }
    }

    /**
     * @param mailMessage
     * @return
     */
    private Message getMessage(final MailMessage mailMessage) {
        Preconditions.checkNotNull(mailMessage, "mailMessage is null");

        logger.info("method=getMessage(), args=[mailMessage=[{}]", mailMessage);

        final MailSettings mailSettings = mailMessage.getMailSettings();
        final String senderAlias = mailSettings.getAlias();
        final String senderMail = mailSettings.getMail();
        final String mailSubject = mailSettings.getSubject();

        try {
            final User targetUser = mailMessage.getTarget();
            final InternetAddress senderAddress = new InternetAddress(senderMail, senderAlias);
            final InternetAddress targetAddress = new InternetAddress(targetUser.getEmail(), targetUser.getName());

            final Message message = new MimeMessage(session);
            message.setFrom(senderAddress);
            message.addRecipient(Message.RecipientType.TO, targetAddress);
            message.setSubject(mailSubject);
            message.setContent(getMultipart(mailMessage));

            return message;

        } catch (MessagingException e) {
            logger.error("method=getMessage() - error creating mail part for user mail=[{}], message=[{}]", mailMessage.getTarget().getEmail(), mailMessage);

        } catch (UnsupportedEncodingException e) {
            logger.error("method=getMessage() - error creating mail part for user mail=[{}], message=[{}]", mailMessage.getTarget().getEmail(), mailMessage);
        }

        return null;
    }

    /**
     * @param mailMessage
     * @return
     */
    private Multipart getMultipart(final MailMessage mailMessage) {
        Preconditions.checkNotNull(mailMessage, "mailMessage is null");

        logger.info("method=getMultipart(), args=[mailMessage=[{}]", mailMessage);

        final Multipart multipart = new MimeMultipart("related");
        addMessagePart(multipart, mailMessage);
        addAttachmentsPart(multipart, mailMessage);

        return multipart;
    }

    /**
     * @param multipart
     * @param mailMessage
     */
    private void addMessagePart(final Multipart multipart, final MailMessage mailMessage) {
        Preconditions.checkNotNull(multipart, "multipart is null");
        Preconditions.checkNotNull(mailMessage, "mailMessage is null");

        logger.info("method=addMessagePart(), args=[multipart=[{}, mailMessage=[{}]]", multipart, mailMessage);

        final MimeBodyPart bodyPart = new MimeBodyPart();
        try {
            bodyPart.setContent(mailMessage.getContent(), CONTENT_TYPE_TEXT_HTML);
            multipart.addBodyPart(bodyPart);

        } catch (MessagingException e) {
            logger.error("method=addMessagePart() - error creating mail part for user mail=[{}], message=[{}]", mailMessage.getTarget().getEmail(), mailMessage);
        }
    }

    /**
     * @param multipart
     * @param mailMessage
     */
    private void addAttachmentsPart(final Multipart multipart, final MailMessage mailMessage) {
        Preconditions.checkNotNull(multipart, "multipart is null");
        Preconditions.checkNotNull(mailMessage, "mailMessage is null");

        logger.info("method=addAttachmentsPart(), args=[multipart=[{}, mailMessage=[{}]]", multipart, mailMessage);

        try {
            final Campaign campaign = mailMessage.getCampaign();
            final Template template = campaign.getTemplate();
            final Set<Attachment> attachments = template.getAttachments();

            if (!attachments.isEmpty()) {

                logger.info("method=addAttachmentsPart() - templateId=[{}] has attachments");

                for (final Attachment attachment : attachments) {
                    final String filename = attachment.getFilename();
                    final String mimeType = attachment.getMimeType().getType();
                    final Blob content = attachment.getContent();
                    final DataSource dataSource = new ByteArrayDataSource(content.getBytes(), mimeType);

                    final MimeBodyPart bodyPart = new MimeBodyPart();
                    bodyPart.setFileName(filename);
                    bodyPart.setDataHandler(new DataHandler(dataSource));
                    bodyPart.setDisposition(MimeBodyPart.INLINE);
                    bodyPart.setHeader(CONTENT_TYPE, mimeType);
                    bodyPart.addHeader(CONTENT_ID, attachment.getContentID());

                    multipart.addBodyPart(bodyPart);
                }
            }

        } catch (MessagingException e) {
            logger.error("method=addMessagePart() - error creating mail part for user mail=[{}], message=[{}]", mailMessage.getTarget().getEmail(), mailMessage);
        }
    }

}
