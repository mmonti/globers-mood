package com.globant.labs.mood.resources;

import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.Frequency;
import org.springframework.data.domain.Sort;

import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface CampaignResource {

    /**
     *
     * @param page
     * @param size
     * @param property
     * @param direction
     * @return
     */
    Response campaigns(final Integer page, final Integer size, final String property, final Sort.Direction direction);

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
     * @return
     */
    Response startCampaign(final long id);

    /**
     *
     * @param id
     * @return
     */
    Response closeCampaign(final long id);
}
