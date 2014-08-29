package com.globant.labs.mood.model.persistent;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.globant.labs.mood.support.jackson.FrequencyDeserializer;
import com.globant.labs.mood.support.jackson.TemplateSerializer;
import com.google.appengine.api.search.checkers.Preconditions;
import com.google.appengine.datanucleus.annotations.Unowned;
import com.google.common.collect.Lists;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

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

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduleDate;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;

    @Enumerated(EnumType.STRING)
    private CampaignStatus status;

    @Unowned
    @OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, optional = true)
    private MailSettings mailSettings = null;

    @Unowned
    @OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Template template;

    @Unowned
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<User> targets = new HashSet<User>();

    @Unowned
    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Feedback> feedbacks = new HashSet<Feedback>();

    @Basic
    private int feedbackNumber = 0;

    @Enumerated(EnumType.STRING)
    private Frequency frequency = Frequency.ONCE;

    @Basic
    private int index = 0;

    @Basic
    private long parentId = 0;

    @Basic
    private boolean tokenEnabled;

    protected Campaign() {
        this(new Date(), CampaignStatus.CREATED);
        this.mailSettings = new MailSettings();
        this.feedbackNumber = 0;
        this.tokenEnabled = Boolean.TRUE;
    }

    protected Campaign(final Date created, final CampaignStatus status) {
        super();
        this.created = created;
        this.status = status;
    }

    public Campaign(final Campaign parent) {
        this();
        this.setName(parent.getName());
        this.setDescription(parent.getDescription());
        this.addTargets(parent.getTargets());
        this.setTemplate(parent.getTemplate());
        this.setFrequency(parent.getFrequency());
        this.setScheduleDate(parent.getFrequency().getScheduleDate(parent.getStartDate()));
        this.setExpirationDate(parent.getFrequency().getExpirationDate(parent.getExpirationDate()));
        this.setParentId(parent.getParentId() == 0 ? parent.getId() : parent.getParentId());
        this.setIndex(parent.getIndex() + 1);
    }

    public int getIndex() {
        return index;
    }

    private void setIndex(int index) {
        this.index = index;
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

    public MailSettings getMailSettings() {
        return mailSettings;
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

    public void addTargets(final Collection<User> targets) {
        Preconditions.checkNotNull(targets, "targets cannot be null");
        if (targets.isEmpty()) {
            return;
        }
        this.targets.addAll(targets);
    }

    public void addFeedback(final Feedback feedback) {
        Preconditions.checkNotNull(feedback, "feedback cannot be null");
        this.feedbacks.add(feedback);
        this.feedbackNumber++;
    }

    @JsonDeserialize(using = FrequencyDeserializer.class)
    public void setFrequency(final Frequency frequency) {
        this.frequency = frequency;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public boolean isRecursive() {
        return !getFrequency().equals(Frequency.ONCE);
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

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isComplete() {
        return (this.targets.size() == this.feedbackNumber);
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public Campaign start() {
        setStatus(CampaignStatus.STARTED);
        setStartDate(new Date());
        return this;
    }

    public Campaign waitForFeedback() {
        setStatus(CampaignStatus.WAITING_FOR_FEEDBACK);
        return this;
    }

    public Campaign close() {
        setStatus(CampaignStatus.CLOSED);
        setEndDate(new Date());
        return this;
    }

    public boolean isClosed() {
        return (CampaignStatus.CLOSED.equals(this.getStatus()));
    }

    public boolean isStarted() {
        return (CampaignStatus.STARTED.equals(this.getStatus()));
    }

    public boolean isWaitingForFeedback() {
        return (CampaignStatus.WAITING_FOR_FEEDBACK.equals(this.getStatus()));
    }

    public boolean isScheduled() {
        return (this.scheduleDate != null & this.startDate == null);
    }

    public boolean isMaster() {
        if (!isRecursive()) {
            return true;
        }
        return (this.index == 0);
    }

    public boolean isTokenEnabled() {
        return tokenEnabled;
    }

    public <T> List<T> collect(final String key, final Class<T> type) {
        final List<T> values = Lists.newArrayList();
        for (final Feedback feedback : feedbacks) {
            T value = feedback.as(key, type);
            if (value != null) {
                values.add(value);
            }
        }
        return values;
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
