package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.events.StatsEvent;
import com.globant.labs.mood.exception.EntityNotFoundException;
import com.globant.labs.mood.exception.ServiceException;
import com.globant.labs.mood.model.StatsEntry;
import com.globant.labs.mood.model.persistent.*;
import com.globant.labs.mood.repository.data.CampaignRepository;
import com.globant.labs.mood.repository.data.FeedbackRepository;
import com.globant.labs.mood.repository.data.ProjectRepository;
import com.globant.labs.mood.repository.data.UserRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.FeedbackService;
import com.google.appengine.api.search.checkers.Preconditions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

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

    @Transactional
    @Override
    public Feedback store(final long campaignId, final long projectId, final String email, final Mood gmv, final Mood cmv, final String comment) {
        Preconditions.checkNotNull(campaignId, "campaignId cannot be null");
        Preconditions.checkNotNull(projectId, "projectId cannot be null");
        Preconditions.checkNotNull(email, "email cannot be null");
        Preconditions.checkNotNull(gmv, "gmv cannot be null");
        Preconditions.checkNotNull(cmv, "cmv cannot be null");

        final Campaign campaign = campaignRepository.findOne(campaignId);
        if (campaign == null) {
            throw new EntityNotFoundException(Campaign.class, campaignId);
        }

        if (CampaignStatus.CLOSED.equals(campaign.getStatus())) {
//            throw new EntityNotFoundException(Campaign.class, campaignId);
        }

        final Project project = projectRepository.findOne(projectId);
        if (project == null) {
            throw new EntityNotFoundException(Project.class, projectId);
        }

        final User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("User with email=[" + email + "] not found");
        }

        final Feedback existentFeedback = feedbackRepository.feedbackAlreadySubmitted(campaign, user);
        if (existentFeedback != null) {
            throw new ServiceException("Feedback of user=[" + email + "] for campaignId=[" + campaignId + "] was already submitted");
        }

        publishAfterCommit(new StatsEvent(this, Feedback.class, StatsEntry.FEEDBACK_COUNT));
        return feedbackRepository.save(new Feedback(campaign, project, user, gmv, cmv, comment));
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Feedback> feedbacks() {
        return new HashSet<Feedback>(feedbackRepository.findAll());
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Feedback> feedbackOfCampaign(final long campaignId) {
        Preconditions.checkNotNull(campaignId, "campaignId cannot be null");

        final Campaign campaign = campaignRepository.findOne(campaignId);
        if (campaign == null) {
            throw new EntityNotFoundException(Campaign.class, campaignId);
        }

        return new HashSet<Feedback>(feedbackRepository.feedbackOfCampaign(campaign));
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Feedback> feedbackOfUser(final long campaignId, final long userId) {
        Preconditions.checkNotNull(userId, "userId cannot be null");

        final User user = userRepository.findOne(userId);
        if (user == null) {
            throw new EntityNotFoundException(User.class, userId);
        }

        return new HashSet<Feedback>(feedbackRepository.feedbackOfUser(user));
    }

}
