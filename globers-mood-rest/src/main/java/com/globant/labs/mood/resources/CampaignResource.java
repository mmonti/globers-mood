package com.globant.labs.mood.resources;

import com.globant.labs.mood.model.persistent.Campaign;
import org.springframework.data.domain.Sort;

import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface CampaignResource {

    /**
     * @param page
     * @param size
     * @param property
     * @param direction
     * @return
     */
    Response campaigns(final Integer page, final Integer size, final String property, final Sort.Direction direction);

    /**
     * @param campaignId
     * @return
     */
    Response campaign(final Long campaignId);

    /**
     * @param campaign
     * @return
     */
    Response addCampaign(final Campaign campaign);

    /**
     * @param campaignId
     * @param userId
     * @return
     */
    Response remind(final Long campaignId, final Long userId);

    /**
     * @param campaignId
     * @return
     */
    Response startCampaign(final Long campaignId);

    /**
     * @param campaignId
     * @return
     */
    Response closeCampaign(final Long campaignId);
}
