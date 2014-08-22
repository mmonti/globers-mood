package com.globant.labs.mood.resources;

import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface StatisticsResource {

    /**
     * @return
     */
    Response datastoreEntities();

    /**
     * @return
     */
    Response weeklyFeedback();

    /**
     * @return
     */
    Response campaignStatistics(final Long campaignId);

}
