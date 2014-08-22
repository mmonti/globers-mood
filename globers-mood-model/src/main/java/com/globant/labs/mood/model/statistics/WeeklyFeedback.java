package com.globant.labs.mood.model.statistics;

import com.google.appengine.repackaged.org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class WeeklyFeedback {

    public static final String DAY_PATTERN = "yyyyMMdd";

    private Long campaignId;
    private Date fromDate;
    private Date toDate;
    private Map<String, Integer> entries;

    /**
     *
     * @param campaignId
     * @param fromDate
     * @param toDate
     */
    public WeeklyFeedback(final Long campaignId, final Date fromDate, final Date toDate) {
        this.campaignId = campaignId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.entries = new HashMap<String, Integer>();
        initialize(this.entries);
    }

    /**
     *
     * @param entries
     */
    private void initialize(final Map<String, Integer> entries) {
        final SimpleDateFormat sdf = new SimpleDateFormat(DAY_PATTERN);
        final DateTime from = new DateTime(fromDate);
        for (int day = 0; day < 7; day++) {
            entries.put(sdf.format(from.plusDays(day).toDate()), 0);
        }
    }

    public void addEntry(final String date, final Integer feedbackNumber) {
        this.entries.put(date, feedbackNumber);
    }

    public boolean contains(final String date) {
        return this.entries.containsKey(date);
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public Map<String, Integer> getEntries() {
        return entries;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }
}
