package com.globant.labs.mood.model.persistent;

import com.google.appengine.api.search.checkers.Preconditions;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public enum CampaignStatus {

    CREATED,
    STARTED,
    WAITING_FOR_FEEDBACK,
    CLOSED;

    private static final Map<String, CampaignStatus> LOOKUP = new HashMap<String, CampaignStatus>();

    static {
        for (final CampaignStatus currentCampaignStatus : EnumSet.allOf(CampaignStatus.class)) {
            LOOKUP.put(String.valueOf(currentCampaignStatus.name()), currentCampaignStatus);
        }
    }

    public static CampaignStatus fromString(final String name) {
        Preconditions.checkNotNull(name, "name cannot be null");
        final CampaignStatus campaignStatus = LOOKUP.get(name);
        if (campaignStatus == null) {
            throw new IllegalArgumentException("The name parameter is invalid=[" + name + "]");
        }
        return campaignStatus;
    }

    /**
     *
     * @param status
     * @return
     */
    public boolean hasPreviousValidStatus(final CampaignStatus status) {
        switch (this) {
            case CREATED:
                return status.equals(CREATED);
            case STARTED:
                return status.equals(STARTED) || status.equals(CREATED);
            case WAITING_FOR_FEEDBACK:
                return status.equals(WAITING_FOR_FEEDBACK) || status.equals(CREATED) || status.equals(STARTED);
            case CLOSED:
                return status.equals(STARTED) || status.equals(CLOSED) || status.equals(WAITING_FOR_FEEDBACK);
            default:
                return false;
        }
    }

}
