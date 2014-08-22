package com.globant.labs.mood.model.statistics;

import java.io.Serializable;

/**
 * Created by mmonti on 8/19/14.
 */
public class CampaignStatistics implements Serializable {

    private Long campaignId;

    public CampaignStatistics(final Long campaignId) {
        this.campaignId = campaignId;
    }
}
