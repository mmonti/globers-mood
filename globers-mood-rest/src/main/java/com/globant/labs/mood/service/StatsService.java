package com.globant.labs.mood.service;

import com.globant.labs.mood.model.stats.WeeklyFeedback;
import com.google.appengine.api.datastore.Entity;

import java.util.List;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface StatsService {

    /**
     * @return
     */
    List<Entity> getMetaData();

    /**
     * @return
     */
    Entity getGlobalStatistics();

    /**
     * @return
     */
    List<WeeklyFeedback> weeklyFeedback();
}
