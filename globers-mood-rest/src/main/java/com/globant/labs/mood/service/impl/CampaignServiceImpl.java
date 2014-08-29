package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.events.DispatchCampaignEvent;
import com.globant.labs.mood.events.DispatchUserEvent;
import com.globant.labs.mood.exception.BusinessException;
import com.globant.labs.mood.model.persistent.*;
import com.globant.labs.mood.repository.data.CampaignRepository;
import com.globant.labs.mood.repository.data.PreferenceRepository;
import com.globant.labs.mood.repository.data.TemplateRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.CampaignService;
import com.google.appengine.api.search.checkers.Preconditions;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.globant.labs.mood.exception.BusinessException.ErrorCode.*;
import static com.globant.labs.mood.model.persistent.CampaignStatus.*;
import static com.globant.labs.mood.support.StringSupport.on;
import static com.google.appengine.repackaged.com.google.common.collect.Lists.newArrayList;
import static com.google.appengine.repackaged.com.google.common.collect.Sets.newHashSet;
import static com.google.common.collect.Iterables.tryFind;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Service
public class CampaignServiceImpl extends AbstractService implements CampaignService {

    private static final Logger logger = LoggerFactory.getLogger(CampaignServiceImpl.class);

    @Inject
    private CampaignRepository campaignRepository;

    @Inject
    private TemplateRepository templateRepository;

    @Inject
    private PreferenceRepository preferenceRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<Campaign> campaigns(final Pageable pageable) {
        logger.info("method=campaigns(), args=[pageable={}]", pageable);
        return campaignRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Campaign store(final Campaign campaign) {
        Preconditions.checkNotNull(campaign, "campaign is null");
        Preconditions.checkNotNull(campaign.getTargets(), "targets is null");
        Preconditions.checkNotNull(campaign.getTemplate(), "template is null");

        logger.info("method=store(), args=[campaign={}]", campaign);

        if (campaign.getId() == null) {
            final Campaign storedCampaign = campaignRepository.campaignByName(campaign.getName());
            if (storedCampaign != null) {
                logger.error("method=store() - campaign name=[{}] already exist", campaign.getName());
                throw new BusinessException(on("Campaign with name=[{}] already exist.", campaign.getName()), EXPECTATION_FAILED);
            }

            if (!CREATED.hasPreviousValidStatus(campaign.getStatus())) {
                logger.error("method=store() - campaignId=[{}] is in invalid state=[{}]", campaign.getId(), campaign.getStatus());
                throw new BusinessException(on("Campaign with name=[{}] is in invalid status=[{}]", campaign.getName(), campaign.getStatus()), ILLEGAL_STATE);
            }
        }

        final Template template = campaign.getTemplate();
        final Template storedTemplate = templateRepository.findOne(template.getId());
        if (storedTemplate == null) {
            logger.error("method=store() - templateId=[{}] of campaignId=[{}] not found", template.getId(), campaign.getId());
            throw new BusinessException(on("Template with id=[{}] of Campaign with id=[{}] not found.", template.getId(), campaign.getId()), RESOURCE_NOT_FOUND);
        }

        logger.info("method=store() - setting templateId=[{}].", storedTemplate.getId());
        campaign.setTemplate(storedTemplate);

        return campaignRepository.save(campaign);
    }

    @Transactional(readOnly = true)
    @Override
    public Campaign campaign(final Long campaignId) {
        Preconditions.checkNotNull(campaignId, "campaignId is null");

        logger.info("method=campaign(), args=[campaignId={}]", campaignId);
        return campaignRepository.findOne(campaignId);
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Campaign> recursiveCampaigns(final Long campaignId) {
        Preconditions.checkNotNull(campaignId, "id cannot be null");

        logger.info("method=recursiveCampaigns(), args=[campaignId={}]", campaignId);
        return newHashSet(campaignRepository.recursiveCampaigns(campaignId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Campaign> mostActive() {
        logger.info("method=mostActive()");
        return campaignRepository.mostActive();
    }

    @Transactional(readOnly = false)
    @Override
    public void waitForFeedback(final Long campaignId) {
        Preconditions.checkNotNull(campaignId, "campaignId is null");

        logger.info("method=waitForFeedback(), args=[campaignId={}]", campaignId);
        final Campaign campaign = campaign(campaignId);
        if (campaign == null) {
            logger.error("method=waitForFeedback) - campaignId=[{}] not found", campaignId);
            throw new BusinessException(on("Campaign with campaignId=[{}] not found.", campaignId), RESOURCE_NOT_FOUND);
        }

        if (!WAITING_FOR_FEEDBACK.hasPreviousValidStatus(campaign.getStatus())) {
            logger.error("method=waitForFeedback() - campaignId=[{}] is in invalid state=[{}]", campaignId, campaign.getStatus());
            throw new BusinessException(on("Campaign with campaignId=[{}] is in invalid state=[{}]", campaignId, campaign.getStatus()), ILLEGAL_STATE);
        }

        logger.info("method=waitForFeedback() - setting campaignId=[{}] to WAIT_FOR_FEEDBACK.", campaign);
        campaignRepository.saveAndFlush(campaign.waitForFeedback());

        // == Recursive Campaign Handling.
        if (campaign.isRecursive()) {
            logger.info("method=waitForFeedback() - campaignId=[{}] is recursive.", campaignId);
            final Campaign recursiveCampaign = campaignRepository.saveAndFlush(new Campaign(campaign));

            logger.info("method=waitForFeedback() - child campaign created with campaignId=[{}]", recursiveCampaign.getId());
        }
    }

    @Transactional(readOnly = false)
    @Override
    public void start(final Long campaignId) {
        Preconditions.checkNotNull(campaignId, "campaignId is null");

        logger.info("method=start(), args=[campaignId={}]", campaignId);

        final Campaign campaign = campaign(campaignId);
        if (campaign == null) {
            logger.error("method=start() - campaignId=[{}] not found", campaignId);
            throw new BusinessException(on("Campaign with campaignId=[{}] not found.", campaignId), RESOURCE_NOT_FOUND);
        }

        if (!STARTED.hasPreviousValidStatus(campaign.getStatus())) {
            logger.error("method=start() - campaignId=[{}] is in invalid state=[{}]", campaignId, campaign.getStatus());
            throw new BusinessException(on("Campaign with campaignId=[{}] is in invalid state=[{}]", campaignId, campaign.getStatus()), ILLEGAL_STATE);
        }

        logger.info("method=start() - setting campaignId=[{}] to STARTED.", campaign);
        campaignRepository.saveAndFlush(campaign.start());

        // = Enqueue a task to dispatch emails
        logger.info("method=start() - triggering dispatching event for campaignId=[{}].", campaignId);
        publish(new DispatchCampaignEvent(this, campaign));
    }

    @Transactional(readOnly = false)
    @Override
    public void close(final Long campaignId) {
        Preconditions.checkNotNull(campaignId, "campaignId is null");

        logger.info("method=close(), args=[campaignId={}]", campaignId);

        final Campaign campaign = campaign(campaignId);
        if (campaign == null) {
            logger.error("method=close() - campaignId=[{}] not found", campaignId);
            throw new BusinessException(on("Campaign with campaignId=[{}] not found.", campaignId), RESOURCE_NOT_FOUND);
        }

        if (!CLOSED.hasPreviousValidStatus(campaign.getStatus())) {
            logger.error("method=close() - campaignId=[{}] is in invalid state=[{}]", campaignId, campaign.getStatus());
            throw new BusinessException(on("Campaign with campaignId=[{}] is an invalid state=[{}]", campaignId, campaign.getStatus()), ILLEGAL_STATE);
        }

        logger.info("method=close() - setting campaignId=[{}] to CLOSED.", campaign);
        campaignRepository.saveAndFlush(campaign.close());
    }

    @Transactional(readOnly = false)
    @Override
    public void scheduledReadyToStart() {
        logger.info("method=scheduleReadyToStart()");

        for (final Campaign currentScheduledCampaign : Sets.filter(newHashSet(campaignRepository.scheduledReadyToStart(new Date())), new Predicate<Campaign>() {
            @Override
            public boolean apply(final Campaign campaign) {
                return campaign.getStatus().equals(CREATED);
            }
        })) {
            final Long campaignId = currentScheduledCampaign.getId();
            logger.info("method=scheduleReadyToStart() - starting scheduled campaignId=[{}]", campaignId);
            start(campaignId);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Campaign> scheduledPendingToStart() {
        logger.info("method=scheduledPendingToStart()");

        final Set<Campaign> pendingToStartCampaigns = Sets.filter(newHashSet(campaignRepository.scheduledPendingToStart(new Date())), new Predicate<Campaign>() {
            @Override
            public boolean apply(Campaign campaign) {
                return campaign.getStatus().equals(CREATED);
            }
        });
        logger.info("method=scheduledPendingToStart() - found [{}] campaigns pending to start", pendingToStartCampaigns.size());
        return pendingToStartCampaigns;
    }

    @Transactional(readOnly = false)
    @Override
    public void scheduledReadyToClose() {
        logger.info("method=scheduledReadyToClose()");

        final List<CampaignStatus> status = newArrayList(CREATED, STARTED, WAITING_FOR_FEEDBACK);
        for (final Campaign currentExpiredCampaign : Sets.filter(newHashSet(campaignRepository.scheduledReadyToClose(new Date())), new Predicate<Campaign>() {
            @Override
            public boolean apply(final Campaign campaign) {
                return status.contains(campaign.getStatus());
            }
        })) {
            final Long campaignId = currentExpiredCampaign.getId();
            logger.info("method=scheduledReadyToClose() - starting scheduled campaignId=[{}]", campaignId);
            close(campaignId);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Campaign> scheduledNextToExpire() {
        logger.debug("method=scheduledNextToExpire()");

        final List<CampaignStatus> status = newArrayList(CREATED, STARTED, WAITING_FOR_FEEDBACK);
        final Set<Campaign> nextToExpireCampaigns = Sets.filter(newHashSet(campaignRepository.scheduledNextToExpire(new Date())), new Predicate<Campaign>() {
            @Override
            public boolean apply(final Campaign campaign) {
                return status.contains(campaign.getStatus());
            }
        });
        logger.info("method=scheduledNextToExpire() - found [{}] campaigns next to expire", nextToExpireCampaigns.size());
        return nextToExpireCampaigns;
    }

    @Transactional(readOnly = false)
    @Override
    public void remind(final Long campaignId, final Long userId) {
        Preconditions.checkNotNull(campaignId, "campaignId is null");
        Preconditions.checkNotNull(userId, "userId is null");

        logger.info("method=remind(), args=[campaignId={}, userId={}]", campaignId, userId);

        final Campaign campaign = campaign(campaignId);
        if (campaign == null) {
            logger.error("method=close() - campaignId=[{}] not found", campaignId);
            throw new BusinessException(on("Campaign with campaignId=[{}] not found.", campaignId), RESOURCE_NOT_FOUND);
        }

        final Optional<User> user = tryFind(campaign.getTargets(), new Predicate<User>() {
            @Override
            public boolean apply(User user) {
                return user.getId().equals(userId);
            }
        });

        if (!user.isPresent()) {
            logger.error("method=remind() - userId=[{}] not found in the campaign targets", userId);
            throw new BusinessException(on("User with userId=[{}] not found in the campaign targets.", userId), RESOURCE_NOT_FOUND);
        }

        publish(new DispatchUserEvent(this, campaign, user.get()));
    }
}
