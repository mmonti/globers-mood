package com.globant.labs.mood.resources;

import com.globant.labs.mood.model.persistent.Mood;

import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface FeedbackResource {

    /**
     *
     * @param campaignId
     * @param email
     * @param token
     * @param globerMoodValue
     * @param clientMoodValue
     * @param comment
     * @return
     */
    Response submitFeedback(final long campaignId, final String email, final String token, final Mood globerMoodValue, final Mood clientMoodValue, final String comment);

    /**
     *
     * @param campaignId
     * @param userId
     * @return
     */
    Response feedbackOfUser(final long campaignId, final long userId);

    /**
     *
     * @param campaignId
     * @return
     */
    Response feedbackOfCampaign(final long campaignId);

}
