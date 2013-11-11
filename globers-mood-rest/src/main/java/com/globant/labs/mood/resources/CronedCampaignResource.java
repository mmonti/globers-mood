package com.globant.labs.mood.resources;

import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface CronedCampaignResource {

    /**
     *
     * @return
     */
    Response startScheduledCampaigns();

    /**
     *
     * @return
     */
    Response closeExpiredCampaigns();
}
