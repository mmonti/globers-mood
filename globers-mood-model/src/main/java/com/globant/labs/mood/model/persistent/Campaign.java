package com.globant.labs.mood.model.persistent;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.globant.labs.mood.jackson.TemplateSerializer;
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

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

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

    protected Campaign() {
        this(new Date(), CampaignStatus.CREATED);
        this.feedbackNumber = 0;
    }

    protected Campaign(final Date created, final CampaignStatus status) {
        super();
        this.created = created;
        this.status = status;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CampaignStatus getStatus() {
        return status;
    }

    private void setStatus(final CampaignStatus status) {
        if (!status.hasPreviousValidStatus(this.getStatus())) {
            throw new IllegalStateException("campaign with id=[{}] has not a valid status=[{}]");
        }
        this.status = status;
    }

    @JsonSerialize(using = TemplateSerializer.class)
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
    }

    public void addFeedback(final Feedback feedback) {
        Preconditions.checkNotNull(feedback, "feedback cannot be null");
        this.feedbacks.add(feedback);
        this.feedbackNumber++;
    }

    public int getFeedbackNumber() {
        return feedbackNumber;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Campaign start() {
        setStatus(CampaignStatus.STARTED);
        return this;
    }

    public Campaign waitForFeedback() {
        setStatus(CampaignStatus.WAITING_FOR_FEEDBACK);
        return this;
    }

    public Campaign close() {
        setStatus(CampaignStatus.CLOSED);
        return this;
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
