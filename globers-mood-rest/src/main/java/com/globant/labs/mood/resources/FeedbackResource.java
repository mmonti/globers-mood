package com.globant.labs.mood.resources;

import com.globant.labs.mood.support.jersey.FeedbackContent;

import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface FeedbackResource {

    /**
     * @param feedbackContent
     * @return
     */
    Response submitFeedback(final FeedbackContent feedbackContent);

    /**
     * @param userId
     * @return
     */
    Response feedbackOfUser(final Long userId);

    /**
     * @param campaignId
     * @param userId
     * @return
     */
    Response feedbackOfUser(final Long campaignId, final Long userId);

    /**
     * @param campaignId
     * @return
     */
    Response feedbackOfCampaign(final Long campaignId);

}
