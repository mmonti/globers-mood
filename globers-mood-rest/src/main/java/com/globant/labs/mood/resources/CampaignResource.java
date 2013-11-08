package com.globant.labs.mood.resources;

import com.globant.labs.mood.model.persistent.Campaign;

import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface CampaignResource {

    /**
     *
     * @param campaign
     * @return
     */
    Response addCampaign(final Campaign campaign);

    /**
     *
     * @return
     */
    Response campaigns();

    /**
     *
     * @param id
     * @return
     */
    Response campaign(final long id);

    /**
     *
     * @param id
     */
    Response startCampaign(final long id);
}
