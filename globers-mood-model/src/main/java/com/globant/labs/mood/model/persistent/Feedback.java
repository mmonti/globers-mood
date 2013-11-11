package com.globant.labs.mood.model.persistent;

import com.google.appengine.datanucleus.annotations.Unowned;
import org.datanucleus.api.jpa.annotations.Extension;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Entity
public class Feedback extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -4177075439722820274L;

    @Unowned
    @ManyToOne
    private Campaign campaign;

    @Unowned
    @OneToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private Mood globerMood;

    @Enumerated(EnumType.STRING)
    private Mood clientMood;

    @Basic
    private String comment;

    public Feedback() {
        super();
    }

    /**
     * @param campaign
     * @param user
     * @param globerMood
     * @param clientMood
     * @param comment
     */
    public Feedback(final Campaign campaign, final User user, final Mood globerMood, final Mood clientMood, final String comment) {
        this();
        this.campaign = campaign;
        this.campaign.addFeedback(this);
        this.user = user;
        this.globerMood = globerMood;
        this.clientMood = clientMood;
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public Mood getGloberMood() {
        return globerMood;
    }

    public Mood getClientMood() {
        return clientMood;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feedback feedback = (Feedback) o;

        if (!getId().equals(feedback.getId())) return false;
        if (!campaign.equals(feedback.campaign)) return false;
        if (!created.equals(feedback.created)) return false;
        if (!user.equals(feedback.user)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = campaign.hashCode();
        result = 31 * result + created.hashCode();
        return result;
    }

}
