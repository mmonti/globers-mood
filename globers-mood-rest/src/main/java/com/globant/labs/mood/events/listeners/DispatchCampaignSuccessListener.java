package com.globant.labs.mood.events.listeners;

import com.globant.labs.mood.events.DispatchCampaignSuccessEvent;
import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.service.CampaignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static com.globant.labs.mood.support.StringSupport.on;

/**
 * Created by mmonti on 8/13/14.
 */
@Component
public class DispatchCampaignSuccessListener extends AbstractEventListener implements ApplicationListener<DispatchCampaignSuccessEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DispatchCampaignSuccessListener.class);

    @Inject
    private CampaignService campaignService;

    /**
     * @param dispatchSuccessEvent
     */
    @Override
    public void onApplicationEvent(final DispatchCampaignSuccessEvent dispatchSuccessEvent) {
        final Campaign campaign = dispatchSuccessEvent.getCampaign();
        logger.info("event - type=[DispatchCampaignSuccessEvent] - campaignId=[{}]", campaign.getId());

        campaignService.waitForFeedback(campaign.getId());
    }
}
