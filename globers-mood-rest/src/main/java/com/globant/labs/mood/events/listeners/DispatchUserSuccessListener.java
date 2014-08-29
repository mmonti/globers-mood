package com.globant.labs.mood.events.listeners;

import com.globant.labs.mood.events.DispatchUserSuccessEvent;
import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.User;
import com.globant.labs.mood.service.CampaignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by mmonti on 8/13/14.
 */
@Component
public class DispatchUserSuccessListener extends AbstractEventListener implements ApplicationListener<DispatchUserSuccessEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DispatchUserSuccessListener.class);

    @Inject
    private CampaignService campaignService;

    /**
     * @param dispatchSuccessEvent
     */
    @Override
    public void onApplicationEvent(final DispatchUserSuccessEvent dispatchSuccessEvent) {
        final Campaign campaign = dispatchSuccessEvent.getCampaign();
        final User user = dispatchSuccessEvent.getUser();
        logger.info("event - type=[DispatchUserSuccessEvent] - campaignId={}, userId={}]", campaign.getId(), user.getId());
    }
}
