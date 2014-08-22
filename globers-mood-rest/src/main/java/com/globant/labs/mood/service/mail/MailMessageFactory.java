package com.globant.labs.mood.service.mail;

import com.globant.labs.mood.exception.BusinessException;
import com.globant.labs.mood.model.mail.MailMessage;
import com.globant.labs.mood.model.mail.MailMessageTemplate;
import com.globant.labs.mood.model.mail.MailSettings;
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

import java.util.HashSet;
import java.util.Set;

import static com.globant.labs.mood.exception.BusinessException.ErrorCode.EXPECTATION_FAILED;
import static com.globant.labs.mood.support.StringSupport.on;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class MailMessageFactory {

    private static final Logger logger = LoggerFactory.getLogger(MailMessageFactory.class);

    private PreferenceService preferenceService;
    private TemplateCompiler templateCompiler;
    private TokenGenerator tokenGenerator;

    /**
     * @param tokenGenerator
     * @param preferenceService
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
        Preconditions.checkNotNull(campaign, "campaign is null");

        final Set<User> targets = campaign.getTargets();
        logger.debug("create - target(s) quantity=[{}]", targets.size());

        final Set<MailMessage> messages = new HashSet();
        for (final User target : targets) {
            final MailMessage mailMessage = create(campaign, target);
            messages.add(mailMessage);
        }
        return messages;
    }

    /**
     * @param campaign
     * @param target
     * @return
     */
    public MailMessage create(final Campaign campaign, final User target) {
        Preconditions.checkNotNull(campaign, "campaign is null");
        Preconditions.checkNotNull(target, "target is null");

        final MailSettings mailSettings = getMailSettings();

        final Set<User> targets = campaign.getTargets();
        if (!targets.contains(target)) {
            throw new BusinessException(on("target with mail=[{}] is not a valid target in this campaign.", target.getEmail()), EXPECTATION_FAILED);
        }

        final MailMessageTemplate mailMessageTemplate = getMailTemplate(campaign.getTemplate());
        final String token = ((UserTokenGenerator) tokenGenerator).getToken(campaign, target);

        return new MailMessage(mailMessageTemplate, mailSettings, token, campaign, target);
    }

    /**
     * @param mailTemplate
     * @return
     */
    private MailMessageTemplate getMailTemplate(final Template mailTemplate) {
        final String templateName = mailTemplate.getName();
        return templateCompiler.compile(templateName);
    }

    /**
     * @return
     */
    public MailSettings getMailSettings() {
        final String alias = preferenceService.preference(PreferenceKey.MAIL_SENDER_ALIAS);
        final String mail = preferenceService.preference(PreferenceKey.MAIL_SENDER);
        final String subject = preferenceService.preference(PreferenceKey.MAIL_SUBJECT);

        logger.debug("create - preferences loaded(alias=[{}], mail=[{}], subject=[{}])", alias, mail, subject);

        Preconditions.checkNotNull(alias, "alias is null");
        Preconditions.checkNotNull(mail, "mail is null");
        Preconditions.checkNotNull(subject, "subject is null");

        return new MailSettings(alias, mail, subject);
    }
}
