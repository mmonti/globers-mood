package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.exception.BusinessException;
import com.globant.labs.mood.model.persistent.*;
import com.globant.labs.mood.repository.data.CampaignRepository;
import com.globant.labs.mood.repository.data.FeedbackRepository;
import com.globant.labs.mood.repository.data.ProjectRepository;
import com.globant.labs.mood.repository.data.UserRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.FeedbackService;
import com.globant.labs.mood.service.mail.token.TokenGenerator;
import com.globant.labs.mood.service.mail.token.UserTokenGenerator;
import com.google.appengine.api.search.checkers.Preconditions;
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

    @Inject
    private FeedbackRepository feedbackRepository;
    @Inject
    private CampaignRepository campaignRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private ProjectRepository projectRepository;
    @Inject
    private TokenGenerator tokenGenerator;

    @Transactional
    @Override
    public Feedback store(final long campaignId, final String email, final String token, final Mood gmv, final Mood cmv, final String comment) {
        Preconditions.checkNotNull(campaignId, "campaignId cannot be null");
        Preconditions.checkNotNull(email, "email cannot be null");
        Preconditions.checkNotNull(token, "token cannot be null");
        Preconditions.checkNotNull(gmv, "gmv cannot be null");
        Preconditions.checkNotNull(cmv, "cmv cannot be null");

        final Campaign campaign = campaignRepository.findOne(campaignId);
        if (campaign == null) {
            throw new BusinessException(on("campaign with campaignId=[{}] not found.", campaignId), RESOURCE_NOT_FOUND);
        }

        if (!CampaignStatus.WAITING_FOR_FEEDBACK.equals(campaign.getStatus())) {
            throw new BusinessException(on("campaign with campaignId=[{}] has an illegal state=[{}]", campaignId, campaign.getStatus()), ILLEGAL_STATE);
        }

        final User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new BusinessException(on("user with email=[{}] not found.", user.getEmail()), RESOURCE_NOT_FOUND);
        }

        final String generatedToken = ((UserTokenGenerator) tokenGenerator).getToken(campaign, user);
        if (!token.equals(generatedToken)) {
            new BusinessException(on("invalid token=[{}]. received token and generated doesnt match.", token), EXPECTATION_FAILED);
        }

        final Feedback existentFeedback = feedbackRepository.feedbackAlreadySubmitted(campaign, user);
        if (existentFeedback != null) {
            throw new BusinessException(on("feedback of user=[{}] for campaignId=[{}] was already submitted", user.getEmail(), campaignId), EXPECTATION_FAILED);
        }

        return feedbackRepository.save(new Feedback(campaign, user, gmv, cmv, comment));
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Feedback> feedbacks() {
        return new HashSet(feedbackRepository.findAll());
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Feedback> feedbackOfCampaign(final long campaignId) {
        Preconditions.checkNotNull(campaignId, "campaignId cannot be null");

        final Campaign campaign = campaignRepository.findOne(campaignId);
        if (campaign == null) {
//            throw new EntityNotFoundException(Campaign.class, campaignId);
        }

        return new HashSet(feedbackRepository.feedbackOfCampaign(campaign));
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Feedback> feedbackOfUser(final long campaignId, final long userId) {
        Preconditions.checkNotNull(userId, "userId cannot be null");

        final User user = userRepository.findOne(userId);
        if (user == null) {
//            throw new EntityNotFoundException(User.class, userId);
        }

        return new HashSet(feedbackRepository.feedbackOfUser(user));
    }

}
