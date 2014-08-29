package com.globant.labs.mood.model.mail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.MailSettings;
import com.globant.labs.mood.model.persistent.User;

import java.io.Serializable;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class MailMessage implements Serializable {

    private static final long serialVersionUID = 5114330117273764214L;

    private MailSettings mailSettings;
    private String token;
    private Campaign campaign;
    private User target;
    private Context context;
    private MailMessageTemplate mailMessageTemplate;

    /**
     * @param mailMessageTemplate
     * @param mailSettings
     * @param campaign
     * @param target
     */
    public MailMessage(final MailMessageTemplate mailMessageTemplate, final MailSettings mailSettings, final String token, final Campaign campaign, final User target) {
        this.mailSettings = mailSettings;
        this.token = token;
        this.campaign = campaign;
        this.target = target;
        this.mailMessageTemplate = mailMessageTemplate;
        this.context = ContextFactory.getInstance();
    }

    public MailSettings getMailSettings() {
        return mailSettings;
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

    @JsonIgnore
    public MailMessageTemplate getMailMessageTemplate() {
        return mailMessageTemplate;
    }

    public String getContent() {
        return this.mailMessageTemplate.eval(this);
    }

}