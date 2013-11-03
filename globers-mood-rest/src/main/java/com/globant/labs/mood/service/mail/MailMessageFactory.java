package com.globant.labs.mood.service.mail;

import com.globant.labs.mood.model.MailMessage;
import com.globant.labs.mood.model.MailMessageTemplate;
import com.globant.labs.mood.model.Sender;
import com.globant.labs.mood.model.persistent.*;
import com.globant.labs.mood.service.PreferenceService;
import com.globant.labs.mood.service.mail.template.TemplateCompiler;
import com.google.appengine.repackaged.com.google.common.base.Preconditions;

import java.util.HashSet;
import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class MailMessageFactory {

    private PreferenceService preferenceService;
    private TemplateCompiler templateCompiler;

    /**
     *
     * @param templateCompiler
     */
    public MailMessageFactory(final PreferenceService preferenceService, final TemplateCompiler templateCompiler) {
        Preconditions.checkNotNull(preferenceService, "preferenceService is null");
        Preconditions.checkNotNull(templateCompiler, "templateCompiler is null");

        this.templateCompiler = templateCompiler;
        this.preferenceService = preferenceService;
    }

    /**
     * @param campaign
     * @return
     */
    public Set<MailMessage> create(final Campaign campaign) {
        final String alias = preferenceService.preference(PreferenceKey.SENDER_ALIAS);
        final String mail = preferenceService.preference(PreferenceKey.SENDER_MAIL);
        final Sender sender = new Sender(alias, mail);

        final Set<User> targets = campaign.getTargets();
        final MailMessageTemplate mailMessageTemplate = getMailTemplate(campaign.getTemplate());

        final Set<MailMessage> messages = new HashSet();
        for (final User currentTarget : targets) {
            messages.add(new MailMessage(sender, campaign, currentTarget, mailMessageTemplate));
        }
        return messages;
    }
//    public Set<MailMessage> create(final Campaign campaign) {
//        final String alias = preferenceService.preference(PreferenceKey.SENDER_ALIAS);
//        final String mail = preferenceService.preference(PreferenceKey.SENDER_MAIL);
//        final Sender sender = new Sender(alias, mail);
//
//        final Set<Project> projects = campaign.getProjects();
//        final MailMessageTemplate mailMessageTemplate = getMailTemplate(campaign.getTemplate());
//
//        final Set<MailMessage> messages = new HashSet<MailMessage>();
//        for (final Project currentProject : projects) {
//            final Set<User> users = currentProject.getUsers();
//
//            for (final User currentUser : users) {
//                messages.add(new MailMessage(sender, campaign, currentProject, currentUser, mailMessageTemplate));
//            }
//        }
//        return messages;
//    }

    /**
     *
     * @param mailTemplate
     * @return
     */
    private MailMessageTemplate getMailTemplate(Template mailTemplate) {
        final String templateName = mailTemplate.getName();
        return templateCompiler.compile(templateName);
    }
}
