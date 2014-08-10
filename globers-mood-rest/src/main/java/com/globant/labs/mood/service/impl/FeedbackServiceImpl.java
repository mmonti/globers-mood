package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.exception.BusinessException;
import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.CampaignStatus;
import com.globant.labs.mood.model.persistent.Feedback;
import com.globant.labs.mood.model.persistent.User;
import com.globant.labs.mood.repository.data.CampaignRepository;
import com.globant.labs.mood.repository.data.FeedbackRepository;
import com.globant.labs.mood.repository.data.UserRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.FeedbackService;
import com.globant.labs.mood.service.mail.token.TokenGenerator;
import com.globant.labs.mood.service.mail.token.UserTokenGenerator;
import com.globant.labs.mood.support.jersey.FeedbackContent;
import com.google.appengine.api.search.checkers.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

import static com.globant.labs.mood.exception.BusinessException.ErrorCode.*;
import static com.globant.labs.mood.support.StringSupport.on;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Service
public class FeedbackServiceImpl extends AbstractService implements FeedbackService {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackServiceImpl.class);

    private static final String FEEDBACK_TOKEN_VALIDATE = "${feedback.token.validate:true}";

    @Inject
    private FeedbackRepository feedbackRepository;

    @Inject
    private CampaignRepository campaignRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private TokenGenerator tokenGenerator;

    @Value(value = FEEDBACK_TOKEN_VALIDATE)
    private boolean validateToken = true;

    @Transactional
    @Override
    public Feedback store(final FeedbackContent feedbackContainer) {
        Preconditions.checkNotNull(feedbackContainer.getCampaignId(), "campaignId cannot be null");
        Preconditions.checkNotNull(feedbackContainer.getEmail(), "email cannot be null");
        Preconditions.checkNotNull(feedbackContainer.getToken(), "token cannot be null");

        final Long campaignId = feedbackContainer.getCampaignId();
        final String email = feedbackContainer.getEmail();
        final String token = feedbackContainer.getToken();

        final Campaign campaign = campaignRepository.findOne(campaignId);
        if (campaign == null) {
            throw new BusinessException(on("campaign with campaignId=[{}] not found.", campaignId), RESOURCE_NOT_FOUND);
        }

        if (!CampaignStatus.WAITING_FOR_FEEDBACK.equals(campaign.getStatus())) {
            final String message = on("campaign with campaignId=[{}] has an illegal state=[{}]", campaignId, campaign.getStatus());
            throw new BusinessException(message, CAMPAIGN_ALREADY_CLOSED, true);
        }

        final User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new BusinessException(on("user with email=[{}] not found.", email), RESOURCE_NOT_FOUND);
        }

        // = Check for token validation (false for DEV / true for PROD).
        if (shouldValidateToken()) {
            final String generatedToken = ((UserTokenGenerator) tokenGenerator).getToken(campaign, user);
            if (!token.equals(generatedToken)) {
                throw new BusinessException(on("invalid token=[{}]. received token and generated doesn't match.", token), EXPECTATION_FAILED);
            }
        }

        final Feedback existentFeedback = feedbackRepository.feedbackAlreadySubmitted(campaign, user);
        if (existentFeedback != null) {
            // = TODO: Look for a more elegant way to handle this exception.
            final String message = on("feedback of user=[{}] for campaignId=[{}] was already submitted", user.getEmail(), campaignId);
            throw new BusinessException(message, FEEDBACK_ALREADY_SUBMITTED, true);
        }

        final Feedback feedback = feedbackRepository.save(new Feedback(campaign, user, feedbackContainer.getAttributes()));

        // = Check if we should close the campaign.
        if (campaign.isCampaignComplete()) {
            campaign.close();
        }

        return feedback;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Feedback> feedbacks(final Pageable pageable) {
        return feedbackRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Feedback> feedbackOfCampaign(final long campaignId) {
        Preconditions.checkNotNull(campaignId, "campaignId cannot be null");

        final Campaign campaign = campaignRepository.findOne(campaignId);
        if (campaign == null) {
            throw new BusinessException(on("campaign with campaignId=[{}] not found", campaignId), RESOURCE_NOT_FOUND);
        }

        return new HashSet(feedbackRepository.feedbackOfCampaign(campaign));
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Feedback> feedbackOfUserCampaign(final long campaignId, final long userId) {
        Preconditions.checkNotNull(userId, "userId cannot be null");
        final User user = userRepository.findOne(userId);
        if (user == null) {
            throw new BusinessException(on("user with userId=[{}] not found", userId), RESOURCE_NOT_FOUND);
        }
        final Campaign campaign = campaignRepository.findOne(campaignId);
        if (campaign == null) {
            throw new BusinessException(on("campaign with campaignId=[{}] not found", campaignId), RESOURCE_NOT_FOUND);
        }
        return new HashSet(feedbackRepository.feedbackOfUserCampaign(campaign, user));
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Feedback> feedbackOfUser(final long userId) {
        Preconditions.checkNotNull(userId, "userId cannot be null");
        final User user = userRepository.findOne(userId);
        if (user == null) {
            throw new BusinessException(on("user with userId=[{}] not found", userId), RESOURCE_NOT_FOUND);
        }
        return new HashSet(feedbackRepository.feedbackOfUser(user));
    }

    /**
     *
     * @return
     */
    public boolean shouldValidateToken() {
        return validateToken;
    }
}