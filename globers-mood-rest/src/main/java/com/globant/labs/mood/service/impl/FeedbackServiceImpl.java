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

    @Inject
    private FeedbackRepository feedbackRepository;

    @Inject
    private CampaignRepository campaignRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private TokenGenerator tokenGenerator;

    @Transactional
    @Override
    public Feedback store(final FeedbackContent feedbackContent) {
        Preconditions.checkNotNull(feedbackContent, "feedbackContent is null");
        Preconditions.checkNotNull(feedbackContent.getCampaignId(), "campaignId is null");
        Preconditions.checkNotNull(feedbackContent.getEmail(), "email is null");
        Preconditions.checkNotNull(feedbackContent.getToken(), "token is null");

        logger.info("method=store(), args=[feedbackContent={}]", feedbackContent);

        final Long campaignId = feedbackContent.getCampaignId();
        final String email = feedbackContent.getEmail();
        final String token = feedbackContent.getToken();

        final Campaign campaign = campaignRepository.findOne(campaignId);
        if (campaign == null) {
            logger.error("method=store() - campaignId=[{}] not found", campaignId);
            throw new BusinessException(on("Campaign with campaignId=[{}] not found.", campaignId), RESOURCE_NOT_FOUND);
        }

        // = TODO : Review the status check.
        if (!CampaignStatus.WAITING_FOR_FEEDBACK.equals(campaign.getStatus())) {
            logger.error("method=store() - campaignId=[{}] is in invalid state=[{}]", campaignId, campaign.getStatus());
            throw new BusinessException(on("Campaign with campaignId=[{}] is in invalid state=[{}]", campaignId, campaign.getStatus()), CAMPAIGN_ALREADY_CLOSED, true);
        }

        final User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.error("method=store() - user mail=[{}] not found", email);
            throw new BusinessException(on("User with email=[{}] not found.", email), RESOURCE_NOT_FOUND);
        }

        // = Check for token validation (false for DEV / true for PROD).
        if (campaign.isTokenEnabled()) {
            final String generatedToken = ((UserTokenGenerator) tokenGenerator).getToken(campaign, user);
            if (!token.equals(generatedToken)) {
                logger.error("method=store() - invalid token=[{}]", token);
                throw new BusinessException(on("Token invalid=[{}]", token), EXPECTATION_FAILED);
            }
        }

        final Feedback existentFeedback = feedbackRepository.feedbackOfUser(campaign, user);
        if (existentFeedback != null) {
            // = TODO: Look for a more elegant way to handle this exception.
            logger.error("method=store() - feedback already submitted for email=[{}] of campaignId=[{}]", email, campaignId);
            throw new BusinessException(on("Feedback of user email=[{}] for campaign with id=[{}] was already submitted", email, campaignId), FEEDBACK_ALREADY_SUBMITTED, true);
        }

        logger.info("method=store(), storing feedback of user=[{}] into campaignId=[{}]", email, campaignId);
        final Feedback feedback = feedbackRepository.save(new Feedback(campaign, user, feedbackContent.getAttributes()));

        // = Check if we should close the campaign.
        if (campaign.isComplete()) {
            logger.info("method=store(), campaignId=[{}] is complete.", campaignId);
            campaign.close();
        }

        return feedback;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Feedback> feedbacks(final Pageable pageable) {
        logger.info("method=feedbacks(), args=[pageable={}]", pageable);

        return feedbackRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Feedback> feedbackOfCampaign(final Long campaignId) {
        Preconditions.checkNotNull(campaignId, "campaignId is null");

        logger.info("method=feedbackOfCampaign(), args=[campaignId={}]", campaignId);

        final Campaign campaign = campaignRepository.findOne(campaignId);
        if (campaign == null) {
            logger.error("method=feedbackOfCampaign() - campaignId=[{}] not found", campaignId);
            throw new BusinessException(on("Campaign with campaignId=[{}] not found.", campaignId), RESOURCE_NOT_FOUND);
        }

        return new HashSet(feedbackRepository.feedbackOfCampaign(campaign));
    }

    @Transactional(readOnly = true)
    @Override
    public Feedback feedbackOfUser(final Long campaignId, final Long userId) {
        Preconditions.checkNotNull(userId, "userId is null");

        logger.info("method=feedbackOfUser(), args=[campaignId={}, userId=[{}]]", campaignId, userId);

        final User user = userRepository.findOne(userId);
        if (user == null) {
            logger.error("method=feedbackOfUser() - userId=[{}] not found", userId);
            throw new BusinessException(on("User with userId=[{}] not found.", userId), RESOURCE_NOT_FOUND);
        }

        final Campaign campaign = campaignRepository.findOne(campaignId);
        if (campaign == null) {
            logger.error("method=feedbackOfUser() - campaignId=[{}] not found", campaignId);
            throw new BusinessException(on("Campaign with campaignId=[{}] not found.", campaignId), RESOURCE_NOT_FOUND);
        }

        return feedbackRepository.feedbackOfUser(campaign, user);
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Feedback> feedbackOfUser(final Long userId) {
        Preconditions.checkNotNull(userId, "userId is null");

        logger.info("method=feedbackOfUser(), args=[userId=[{}]]", userId);

        final User user = userRepository.findOne(userId);
        if (user == null) {
            logger.error("method=feedbackOfUser() - userId=[{}] not found", userId);
            throw new BusinessException(on("User with userId=[{}] not found.", userId), RESOURCE_NOT_FOUND);
        }

        return new HashSet(feedbackRepository.feedbackOfUser(user));
    }

}