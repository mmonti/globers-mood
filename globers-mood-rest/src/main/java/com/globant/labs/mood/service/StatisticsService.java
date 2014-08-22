package com.globant.labs.mood.service;

import com.globant.labs.mood.model.statistics.CampaignStatistics;
import com.globant.labs.mood.model.statistics.WeeklyFeedback;
import com.google.appengine.api.datastore.Entity;

import java.util.List;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface StatisticsService {

    /**
     * @return
     */
    List<Entity> datastoreEntities();

    /**
     * @return
     */
    List<WeeklyFeedback> weeklyFeedback();

    /**
     * @param campaignId
     * @return
     */
    CampaignStatistics campaignStatistics(final Long campaignId);
}
