package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.events.MailMessageEvent;
import com.globant.labs.mood.events.StatsEvent;
import com.globant.labs.mood.model.MailMessage;
import com.globant.labs.mood.model.StatsEntry;
import com.globant.labs.mood.model.persistent.Project;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.MailingService;
import com.google.appengine.repackaged.com.google.common.collect.Sets;
import com.google.common.net.MediaType;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Service
public class MailingServiceImpl extends AbstractService implements MailingService {

    @Inject
    private Session session;

    /**
     *
     * @param mailMessages
     * @return
     */
    public int dispatch(final Set<MailMessage> mailMessages) {
        final Set<MailMessage> pendingMailMessages = new HashSet<MailMessage>();
        for (final MailMessage currentMailMessage : mailMessages) {
            final Message message = getMessage(currentMailMessage);
            if (message != null) {
                try {
                    Transport.send(message);

                } catch (MessagingException e) {
                    pendingMailMessages.add(currentMailMessage);
                }

            } else {
                pendingMailMessages.add(currentMailMessage);
            }
        }

        final Set<MailMessage> sent = Sets.difference(mailMessages, pendingMailMessages);
        publishAfterCommit(new MailMessageEvent<MailMessage>(this, sent));

        return sent.size();
    }

    /**
     *
     * @param mailMessage
     * @return
     */
    private Message getMessage(final MailMessage mailMessage) {
        try {
            final Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(getMessagePart(mailMessage));

            final InternetAddress sender = new InternetAddress("monti.mauro@gmail.com", "Administrator");
            final InternetAddress target = new InternetAddress(mailMessage.getUser().getEmail(), mailMessage.getUser().getName());

            final Message message = new MimeMessage(session);
            message.setFrom(sender);
            message.addRecipient(Message.RecipientType.TO, target);
            message.setSubject("Please tell us how you're doing");
            message.setContent(multipart);

            return message;

        } catch (MessagingException e) {

        } catch (UnsupportedEncodingException e) {

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
            bodyPart.setContent(currentMailMessage.getContent(), MediaType.HTML_UTF_8.type());

        } catch (MessagingException e) {

        }
        return bodyPart;
    }
}
