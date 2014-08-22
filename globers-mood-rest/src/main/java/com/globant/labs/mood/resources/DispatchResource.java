package com.globant.labs.mood.resources;

import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface DispatchResource {

    /**
     * @param campaignId
     * @return
     */
    Response dispatchMailMessages(final Long campaignId);

    /**
     * @param campaignId
     * @param userId
     * @return
     */
    Response dispatchMailMessages(final Long campaignId, final Long userId);
}
