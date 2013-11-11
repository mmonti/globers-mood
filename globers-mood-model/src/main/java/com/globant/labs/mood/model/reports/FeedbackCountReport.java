package com.globant.labs.mood.model.reports;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class FeedbackCountReport {

    private Long campaignId;
    private Date fromDate;
    private Date toDate;
    private Map<Date, Integer> entries;

    public FeedbackCountReport(final Long campaignId, final Date fromDate, final Date toDate) {
        this.campaignId = campaignId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.entries = new HashMap<Date, Integer>();
    }

    public void addEntry(Date date, Integer feedbackNumber) {
        this.entries.put(date, feedbackNumber);
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public Map<Date, Integer> getEntries() {
        return entries;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }
}
