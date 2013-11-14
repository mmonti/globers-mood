package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.model.MailMessage;
import com.globant.labs.mood.model.Sender;
import com.globant.labs.mood.model.persistent.User;
import com.globant.labs.mood.repository.data.PreferenceRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.MailingService;
import com.google.appengine.repackaged.com.google.common.collect.Sets;
import com.google.common.net.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.*;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Service
public class MailingServiceImpl extends AbstractService implements MailingService {

    private static final Logger logger = LoggerFactory.getLogger(MailingServiceImpl.class);

    private static final String CONTENT_TYPE_TEXT_HTML = "text/html";

    @Inject
    private PreferenceRepository preferenceRepository;
    private Session session = Session.getDefaultInstance(new Properties(), null);

    /**
     *
     * @param mailMessages
     * @return
     */
    public int dispatch(final Set<MailMessage> mailMessages) {
        logger.info("dispatch - session=[{}]", session);
        final Set<MailMessage> pendingMailMessages = new HashSet();
        for (final MailMessage currentMailMessage : mailMessages) {
            final Message message = getMessage(currentMailMessage);
            if (message != null) {
                try {
                    logger.info("dispatch - sending message=[{}]", message);
                    Transport.send(message);

                } catch (MessagingException e) {
                    logger.info("dispatch - error sending message=[{}]", message);
                    pendingMailMessages.add(currentMailMessage);
                }
            } else {
                logger.info("dispatch - message null, adding to pendings. Message=[{}]", currentMailMessage);
                pendingMailMessages.add(currentMailMessage);
            }
        }

        final Set<MailMessage> sent = Sets.difference(mailMessages, pendingMailMessages);
        logger.debug("dispatch - {} message(s) sent", sent.size());
        logger.debug("dispatch - {} message(s) pending to be sent", pendingMailMessages.size());

        return sent.size();
    }

    /**
     *
     * @param mailMessage
     * @return
     */
    private Message getMessage(final MailMessage mailMessage) {
        logger.debug("get-message - creating mail part...");

        final Sender sender = mailMessage.getSender();
        final String senderAlias = sender.getAlias();
        final String senderMail = sender.getMail();

        try {
            final Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(getMessagePart(mailMessage));

            final User targetUser = mailMessage.getTarget();
            final InternetAddress senderAddress = new InternetAddress(senderMail, senderAlias);
            final InternetAddress targetAddress = new InternetAddress(targetUser.getEmail(), targetUser.getName());

            final Message message = new MimeMessage(session);
            message.setFrom(senderAddress);
            message.addRecipient(Message.RecipientType.TO, targetAddress);
            message.setSubject(mailMessage.getSubject());
            message.setContent(multipart);

            return message;

        } catch (MessagingException e) {
            logger.debug("get-message - error creating mail part", e);

        } catch (UnsupportedEncodingException e) {
            logger.debug("get-message - error creating mail part", e);
        }
        return null;
    }

    /**
     *
     * @param currentMailMessage
     * @return
     */
    private MimeBodyPart getMessagePart(final MailMessage currentMailMessage) {
        final MimeBodyPart bodyPart = new MimeBodyPart();
        try {
            bodyPart.setContent(currentMailMessage.getContent(), CONTENT_TYPE_TEXT_HTML);

        } catch (MessagingException e) {

        }
        return bodyPart;
    }
}
