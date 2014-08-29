package com.globant.labs.mood.model.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.TemplateMetadata;
import com.google.appengine.repackaged.com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mmonti on 8/19/14.
 */
public class CampaignStatistics implements Serializable {

    private static final String CONSTANT_KEY_FREQUENCIES = "frequencies";
    private static final String CONSTANT_KEY_DESCRIPTIVE = "descriptive";

    private Campaign campaign;
    private Map<String, Object> statistics;

    /**
     *
     * @param campaign
     */
    public CampaignStatistics(final Campaign campaign) {
        this.campaign = campaign;
        this.statistics = new HashMap<String, Object>();
    }

    @JsonIgnore
    public Campaign getCampaign() {
        return campaign;
    }

    public Long getCampaignId() {
        return campaign.getId();
    }

    public void addFrequencies(final Map<String, Object> statistics) {
        this.statistics.put(CONSTANT_KEY_FREQUENCIES, statistics);
    }

    public void addDescriptive(final Map<String, Object> statistics) {
        this.statistics.put(CONSTANT_KEY_DESCRIPTIVE, statistics);
    }

    public Map<String, Object> getFrequencies() {
        return (Map<String, Object>) statistics.get(CONSTANT_KEY_FREQUENCIES);
    }

    public Map<String, Object> getDescriptive() {
        return (Map<String, Object>) statistics.get(CONSTANT_KEY_DESCRIPTIVE);
    }
}
