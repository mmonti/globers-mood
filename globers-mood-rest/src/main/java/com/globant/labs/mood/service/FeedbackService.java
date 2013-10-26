package com.globant.labs.mood.service;

import com.globant.labs.mood.model.persistent.Feedback;
import com.globant.labs.mood.model.persistent.Mood;

import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface FeedbackService {

    /**
     * @param campaignId
     * @param projectId
     * @param email
     * @param gmv
     * @param cmv
     * @param comment
     * @return
     */
    Feedback store(final long campaignId, final long projectId, final String email, final Mood gmv, final Mood cmv, final String comment);

    /**
     * @return
     */
    Set<Feedback> feedbacks();

    /**
     * @param campaignId
     * @return
     */
    Set<Feedback> feedbackOfCampaign(final long campaignId);

    /**
     *
     * @param campaignId
     * @param userId
     * @return
     */
    Set<Feedback> feedbackOfUser(final long campaignId, final long userId);
}
