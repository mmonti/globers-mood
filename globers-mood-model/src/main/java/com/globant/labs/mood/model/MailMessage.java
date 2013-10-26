package com.globant.labs.mood.model;

import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.Project;
import com.globant.labs.mood.model.persistent.User;

import java.io.Serializable;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class MailMessage implements Serializable {

    private static final long serialVersionUID = 5114330117273764214L;

    private Sender sender;
    private Campaign campaign;
    private Project project;
    private User user;
    private MailMessageTemplate mailMessageTemplate;

    /**
     *
     * @param sender
     * @param campaign
     * @param project
     * @param user
     * @param mailMessageTemplate
     */
    public MailMessage(final Sender sender, final Campaign campaign, final Project project, final User user, final MailMessageTemplate mailMessageTemplate) {
        this.sender = sender;
        this.campaign = campaign;
        this.project = project;
        this.user = user;
        this.mailMessageTemplate = mailMessageTemplate;
    }

    public Sender getSender() {
        return sender;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public Project getProject() {
        return project;
    }

    public User getUser() {
        return user;
    }

    public MailMessageTemplate getMailMessageTemplate() {
        return mailMessageTemplate;
    }

    public String getContent() {
        return this.mailMessageTemplate.eval(this);
    }

}