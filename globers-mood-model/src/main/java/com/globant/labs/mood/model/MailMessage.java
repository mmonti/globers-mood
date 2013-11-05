package com.globant.labs.mood.model;

import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.Project;
import com.globant.labs.mood.model.persistent.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class MailMessage implements Serializable {

    private static final long serialVersionUID = 5114330117273764214L;

    private Sender sender;
    private String subject;
    private String token;
    private Campaign campaign;
    private User target;
    private Context context;
    private MailMessageTemplate mailMessageTemplate;

    /**
     *
     * @param mailMessageTemplate
     * @param sender
     * @param subject
     * @param token
     * @param campaign
     * @param target
     */
    public MailMessage(final MailMessageTemplate mailMessageTemplate, final Sender sender, final String subject, final String token, final Campaign campaign, final User target) {
        this.sender = sender;
        this.subject = subject;
        this.token = token;
        this.campaign = campaign;
        this.target = target;
        this.mailMessageTemplate = mailMessageTemplate;
        this.context = ContextFactory.getInstance();
    }

    public Sender getSender() {
        return sender;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public User getTarget() {
        return target;
    }

    public Context getContext() {
        return context;
    }

    public String getToken() {
        return token;
    }

    public String getSubject() {
        return subject;
    }

    public MailMessageTemplate getMailMessageTemplate() {
        return mailMessageTemplate;
    }

    public String getContent() {
        return this.mailMessageTemplate.eval(this);
    }

}