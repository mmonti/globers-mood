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
    private Campaign campaign;
//    private Project project;
    private User target;
    private MailMessageTemplate mailMessageTemplate;

    private Map<String, Object> context;

    /**
     *
     * @param sender
     * @param campaign
//     * @param project
     * @param user
     * @param mailMessageTemplate
     */
    public MailMessage(final Sender sender, final Campaign campaign, /*final Project project,*/ final User target, final MailMessageTemplate mailMessageTemplate) {
        this.sender = sender;
        this.campaign = campaign;
//        this.project = project;
        this.target = target;
        this.mailMessageTemplate = mailMessageTemplate;
        this.context = new HashMap<String, Object>();
    }

    public Sender getSender() {
        return sender;
    }

    public Campaign getCampaign() {
        return campaign;
    }

//    public Project getProject() {
//        return project;
//    }


    public User getTarget() {
        return target;
    }

    public MailMessageTemplate getMailMessageTemplate() {
        return mailMessageTemplate;
    }

    public String getContent() {
        return this.mailMessageTemplate.eval(this);
    }

}