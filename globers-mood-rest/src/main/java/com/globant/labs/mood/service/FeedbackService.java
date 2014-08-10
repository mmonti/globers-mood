package com.globant.labs.mood.service;

import com.globant.labs.mood.model.persistent.Feedback;
import com.globant.labs.mood.support.jersey.FeedbackContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface FeedbackService {

    /**
     *
     * @param container
     * @return
     */
    Feedback store(final FeedbackContent container);

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
     * @param userId
     * @return
     */
    Set<Feedback> feedbackOfUser(final long userId);

    /**
     *
     * @param campaignId
     * @param userId
     * @return
     */
    Set<Feedback> feedbackOfUserCampaign(final long campaignId, final long userId);

}
