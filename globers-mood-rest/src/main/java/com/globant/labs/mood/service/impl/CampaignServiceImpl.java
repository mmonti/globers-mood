package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.exception.BusinessException;
import com.globant.labs.mood.model.DispatchResult;
import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.CampaignStatus;
import com.globant.labs.mood.repository.data.CampaignRepository;
import com.globant.labs.mood.repository.data.FeedbackRepository;
import com.globant.labs.mood.repository.data.PreferenceRepository;
import com.globant.labs.mood.repository.data.TemplateRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.CampaignService;
import com.globant.labs.mood.service.mail.MailMessageFactory;
import com.google.appengine.api.search.checkers.Preconditions;
import com.google.appengine.repackaged.com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.globant.labs.mood.exception.BusinessException.ErrorCode.EXPECTATION_FAILED;
import static com.globant.labs.mood.exception.BusinessException.ErrorCode.ILLEGAL_STATE;
import static com.globant.labs.mood.support.StringSupport.on;

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
    private FeedbackRepository feedbackRepository;
    @Inject
    private PreferenceRepository preferenceRepository;
    @Inject
    private MailingServiceImpl mailingService;
    @Inject
    private MailMessageFactory mailMessageFactory;

    @Transactional(readOnly = true)
    @Override
    public Set<Campaign> campaigns() {
        logger.debug("campaigns - querying campaigns");
        return new HashSet<Campaign>(campaignRepository.findAll());
    }

    @Override
    public Page<Campaign> campaigns(final Pageable pageable) {
        return this.campaignRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Campaign store(final Campaign campaign) {
        Preconditions.checkNotNull(campaign, "campaign cannot be null");
        final Campaign storedCampaign = this.campaignRepository.campaignByName(campaign.getName());
        if (storedCampaign != null) {
            logger.debug("store - campaign with name=[{}] already existent", campaign.getName());
            throw new BusinessException(on("campaign with name=[{}] already existent.", campaign.getName()), EXPECTATION_FAILED);
        }
        return campaignRepository.save(campaign);
    }

    @Transactional(readOnly = true)
    @Override
    public Campaign campaign(final long id) {
        Preconditions.checkNotNull(id, "id cannot be null");
        logger.debug("campaign - querying campaign by id=[{}]", id);
        return campaignRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Campaign> mostActive() {
        return this.campaignRepository.mostActive();
    }

    @Transactional
    @Override
    public void start(final long campaignId) {
        final Campaign campaign = this.campaignRepository.findOne(campaignId);
        if (campaign == null) {
            logger.error("start - campaign with id=[{}] not found", campaignId);
            throw new BusinessException(on("campaign with campaignId=[{}] already existent.", campaignId), EXPECTATION_FAILED);
        }

        if (!CampaignStatus.STARTED.hasPreviousValidStatus(campaign.getStatus())) {
            logger.error("start - campaign with id=[{}] has an invalid status=[{}]", campaignId, campaign.getStatus());
            throw new BusinessException(on("campaign with campaignId=[{}] has an illegal state=[{}]", campaignId, campaign.getStatus()), ILLEGAL_STATE);
        }

        logger.debug("start - changing status of campaign with id=[{}] to START.", campaignId);
        campaignRepository.saveAndFlush(campaign.start());

        // == TODO: Has to be an AsynchTask.
        logger.info("start - dispatching emails of campaign id=[{}]", campaignId);
        final DispatchResult dispatchResult = mailingService.dispatch(mailMessageFactory.create(campaign));
        if (dispatchResult.hasPendingNotifications()) {

        }
        logger.debug("start - changing status of campaign with id=[{}] to WAITING_FOR_FEEDBACK.", campaignId);
        campaignRepository.saveAndFlush(campaign.waitForFeedback());
    }

    @Transactional
    @Override
    public void close(final long campaignId) {
        final Campaign campaign = this.campaignRepository.findOne(campaignId);
        if (campaign == null) {
            logger.debug("close - campaign with id=[{}] not found", campaignId);
            throw new BusinessException(on("campaign with campaignId=[{}] already exist.", campaignId), EXPECTATION_FAILED);
        }

        if (!CampaignStatus.CLOSED.hasPreviousValidStatus(campaign.getStatus())) {
            logger.debug("close - campaign with id=[{}] has an invalid status=[{}]", campaignId, campaign.getStatus());
            throw new BusinessException(on("campaign with campaignId=[{}] in illegal state=[{}].", campaignId, campaign.getStatus()), ILLEGAL_STATE);
        }
        campaignRepository.saveAndFlush(campaign.close());
    }

    @Transactional
    @Override
    public void scheduledReadyToStart() {
        final Calendar now = Calendar.getInstance();
        for (final Campaign currentScheduledCampaign : this.campaignRepository.scheduledReadyToStart(now.getTime())) {
            final long campaignId = currentScheduledCampaign.getId();
            logger.debug("startScheduledCampaigns - starting scheduled campaign=[id={}]", campaignId);
            start(currentScheduledCampaign.getId());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Campaign> scheduledPendingToStart() {
        final Calendar now = Calendar.getInstance();
        final Set<Campaign> pendingToStartCampaigns = Sets.newHashSet(this.campaignRepository.scheduledPendingToStart(now.getTime()));
        logger.debug("scheduledStartPendingCampaigns - size=[{}]", pendingToStartCampaigns.size());
        return pendingToStartCampaigns;
    }

    @Transactional
    @Override
    public void scheduledReadyToClose() {
        final Calendar now = Calendar.getInstance();
        for (final Campaign currentExpiredCampaign : this.campaignRepository.scheduledReadyToClose(now.getTime())) {
            final long campaignId = currentExpiredCampaign.getId();
            logger.debug("closeExpiredCampaings - closing expired campaign=[id={}]", campaignId);
            close(campaignId);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Campaign> scheduledNextToExpire() {
        final Calendar now = Calendar.getInstance();
        final Set<Campaign> nextToExpireCampaigns = Sets.newHashSet(this.campaignRepository.scheduledNextToExpire(now.getTime()));
        logger.debug("nextToExpireCampaigns - size=[{}]", nextToExpireCampaigns.size());
        return nextToExpireCampaigns;
    }
}
