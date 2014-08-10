package com.globant.labs.mood.resources;

import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface StatsResource {

    /**
     *
     * @return
     */
    Response statistics();

    /**
     * @return
     */
    Response metadata();

    /**
     * @return
     */
    Response weeklyFeedback();

}
