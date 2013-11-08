package com.globant.labs.mood.model.persistent;

import com.google.appengine.api.search.checkers.Preconditions;
import com.google.appengine.datanucleus.annotations.Unowned;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Entity
public class Campaign extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 882959936672214003L;

    @Basic
    private String name;

    @Basic
    private String description;

    @Temporal(TemporalType.DATE)
    private Date created;

    @Enumerated(EnumType.STRING)
    private CampaignStatus status;

    @Unowned
    @OneToOne(cascade = CascadeType.ALL)
    private Template template;

    @Unowned
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<User> targets = new HashSet<User>();

    @Unowned
    @OneToMany(mappedBy =  "campaign", fetch = FetchType.LAZY)
    private Set<Feedback> feedbacks = new HashSet<Feedback>();

    @Basic
    private int feedbackNumber = 0;

    @Basic
    private int targetNumber = 0;

    public Campaign() {
        this.created = new Date();
        this.status = CampaignStatus.CREATED;
        this.feedbackNumber = 0;
        this.targetNumber = 0;
    }

    /**
     * @param name
     */
    public Campaign(final String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CampaignStatus getStatus() {
        return status;
    }

    public void setStatus(CampaignStatus status) {
        this.status = status;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(final Template template) {
        this.template = template;
    }

    public Set<User> getTargets() {
        return Collections.unmodifiableSet(targets);
    }

    public Set<Feedback> getFeedbacks() {
        return Collections.unmodifiableSet(feedbacks);
    }

    public void addTarget(final User target) {
        Preconditions.checkNotNull(target, "target cannot be null");
        this.targets.add(target);
        this.targetNumber++;
    }

    public void addFeedback(final Feedback feedback) {
        Preconditions.checkNotNull(feedback, "feedback cannot be null");
        this.feedbacks.add(feedback);
        this.feedbackNumber++;
    }

    public int getFeedbackNumber() {
        return feedbackNumber;
    }

    public int getTargetNumber() {
        return targetNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Campaign campaign = (Campaign) o;

        if (!getId().equals(campaign.getId())) return false;
        if (!created.equals(campaign.created)) return false;
        if (!name.equals(campaign.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + created.hashCode();
        return result;
    }

}
