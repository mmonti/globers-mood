package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.events.StatsEvent;
import com.globant.labs.mood.exception.EntityNotFoundException;
import com.globant.labs.mood.model.MailMessage;
import com.globant.labs.mood.model.StatsEntry;
import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.CampaignStatus;
import com.globant.labs.mood.repository.data.CampaignRepository;
import com.globant.labs.mood.repository.data.FeedbackRepository;
import com.globant.labs.mood.repository.data.PreferenceRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.CampaignService;
import com.globant.labs.mood.service.mail.MailMessageFactory;
import com.google.appengine.api.search.checkers.Preconditions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.*;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Service
public class CampaignServiceImpl extends AbstractService implements CampaignService {

    @Inject
    private CampaignRepository campaignRepository;
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
        return new HashSet<Campaign>(campaignRepository.findAll());
    }

    @Transactional
    @Override
    public Campaign store(final Campaign campaign) {
        Preconditions.checkNotNull(campaign, "campaign cannot be null");
        publishAfterCommit(new StatsEvent(this, Campaign.class, StatsEntry.CAMPAIGN_COUNT));
        return campaignRepository.save(campaign);
    }

    @Transactional(readOnly = true)
    @Override
    public Campaign campaign(final Long id) {
        Preconditions.checkNotNull(id, "id cannot be null");
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
            throw new EntityNotFoundException(Campaign.class, campaignId);
        }

        if (CampaignStatus.STARTED.isPreviousStatusValid(campaign.getStatus())) {
            final Set<MailMessage> messages = mailMessageFactory.create(campaign);
            mailingService.dispatch(messages);
        } else {
            throw new IllegalStateException("campaign with id=[{}] has not a valid status=[{}]");
        }
    }
}