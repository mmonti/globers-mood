package com.globant.labs.mood.service;

import com.globant.labs.mood.model.persistent.Feedback;
import com.globant.labs.mood.model.persistent.Mood;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface FeedbackService {

    /**
     *
     * @param campaignId
     * @param email
     * @param token
     * @param gmv
     * @param cmv
     * @param comment
     * @return
     */
    Feedback store(final long campaignId, final String email, final String token, final Mood gmv, final Mood cmv, final String comment);

    /**
     * @return
     */
    Page<Feedback> feedbacks(final Pageable pageable);

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
