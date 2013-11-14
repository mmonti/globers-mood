package com.globant.labs.mood.service.mail;

import com.globant.labs.mood.model.MailMessage;
import com.globant.labs.mood.model.MailMessageTemplate;
import com.globant.labs.mood.model.Sender;
import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.PreferenceKey;
import com.globant.labs.mood.model.persistent.Template;
import com.globant.labs.mood.model.persistent.User;
import com.globant.labs.mood.service.PreferenceService;
import com.globant.labs.mood.service.mail.template.TemplateCompiler;
import com.globant.labs.mood.service.mail.token.TokenGenerator;
import com.globant.labs.mood.service.mail.token.UserTokenGenerator;
import com.google.appengine.repackaged.com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class MailMessageFactory {

    private static final Logger logger = LoggerFactory.getLogger(MailMessageFactory.class);

    private PreferenceService preferenceService;
    private TemplateCompiler templateCompiler;
    private TokenGenerator tokenGenerator;

    /**
     *
     * @param templateCompiler
     */
    public MailMessageFactory(final TokenGenerator tokenGenerator, final PreferenceService preferenceService, final TemplateCompiler templateCompiler) {
        Preconditions.checkNotNull(preferenceService, "preferenceService is null");
        Preconditions.checkNotNull(templateCompiler, "templateCompiler is null");
        Preconditions.checkNotNull(tokenGenerator, "tokenGenerator is null");

        this.templateCompiler = templateCompiler;
        this.preferenceService = preferenceService;
        this.tokenGenerator = tokenGenerator;
    }

    /**
     * @param campaign
     * @return
     */
    public Set<MailMessage> create(final Campaign campaign) {
        final String alias = preferenceService.preference(PreferenceKey.SENDER_ALIAS);
        final String mail = preferenceService.preference(PreferenceKey.SENDER_MAIL);
        final String subject = preferenceService.preference(PreferenceKey.MAIL_SUBJECT);

        logger.debug("create - preferences loaded(alias=[{}], mail=[{}], subject=[{}])", alias, mail, subject);

        Preconditions.checkNotNull(alias, "alias is null");
        Preconditions.checkNotNull(mail, "mail is null");
        Preconditions.checkNotNull(subject, "subject is null");
        final Sender sender = new Sender(alias, mail);

        final Set<User> targets = campaign.getTargets();
        logger.debug("create - target(s) amount=[{}]", targets.size());
        final MailMessageTemplate mailMessageTemplate = getMailTemplate(campaign.getTemplate());

        final Set<MailMessage> messages = new HashSet();
        for (final User currentTarget : targets) {
            final String token = ((UserTokenGenerator) tokenGenerator).getToken(campaign, currentTarget);
            messages.add(new MailMessage(mailMessageTemplate, sender, subject, token, campaign, currentTarget));
        }
        return messages;
    }

    /**
     *
     * @param mailTemplate
     * @return
     */
    private MailMessageTemplate getMailTemplate(final Template mailTemplate) {
        final String templateName = mailTemplate.getName();
        return templateCompiler.compile(templateName);
    }
}
