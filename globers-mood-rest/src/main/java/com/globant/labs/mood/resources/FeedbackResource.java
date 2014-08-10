package com.globant.labs.mood.resources;

import com.globant.labs.mood.support.jersey.FeedbackContent;

import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface FeedbackResource {

    /**
     *
     * @param feedbackContainer
     * @return
     */
    Response submitFeedback(final FeedbackContent feedbackContainer);

    /**
     *
     * @param userId
     * @return
     */
    Response feedbackOfUser(final long userId);

    /**
     *
     * @param campaignId
     * @param userId
     * @return
     */
    Response feedbackOfUserCampaign(final long campaignId, final long userId);

    /**
     *
     * @param campaignId
     * @return
     */
    Response feedbackOfCampaign(final long campaignId);

}
