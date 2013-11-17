package com.globant.labs.mood.resources;

import com.globant.labs.mood.model.persistent.Campaign;

import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface CampaignResource {

    /**
     *
     * @param page
     * @param size
     * @return
     */
    Response campaigns(final Integer page, final Integer size);

    /**
     *
     * @param id
     * @return
     */
    Response campaign(final long id);

    /**
     *
     * @param campaign
     * @return
     */
    Response addCampaign(final Campaign campaign);

    /**
     *
     * @param id
     */
    Response startCampaign(final long id);

    /**
     *
     * @param id
     * @return
     */
    Response closeCampaign(final long id);
}
