package com.globant.labs.mood.events.listeners;

import com.globant.labs.mood.events.DispatchCampaignEvent;
import com.globant.labs.mood.events.DispatchUserEvent;
import com.globant.labs.mood.model.persistent.Campaign;
import com.google.appengine.api.taskqueue.TaskOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.globant.labs.mood.support.StringSupport.on;
import static com.google.appengine.api.taskqueue.QueueFactory.getDefaultQueue;
import static com.google.appengine.api.taskqueue.RetryOptions.Builder.withTaskRetryLimit;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withTaskName;
import static com.google.appengine.api.taskqueue.TaskOptions.Method.POST;

/**
 * Created by mmonti on 8/13/14.
 */
@Component
public class DispatchCampaignListener extends AbstractEventListener implements ApplicationListener<DispatchCampaignEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DispatchCampaignListener.class);

    private static final String DISPATCH_MAIL_MESSAGES_URL = "/rest/api/v1/dispatch/campaign/{}";

    /**
     * @param dispatchEvent
     */
    @Override
    public void onApplicationEvent(final DispatchCampaignEvent dispatchEvent) {
        final Campaign campaign = dispatchEvent.getCampaign();
        logger.info("event - type=[DispatchCampaignEvent] - campaignId=[{}]", campaign.getId());

        enqueueTask(DispatchCampaignEvent.class, POST, on(DISPATCH_MAIL_MESSAGES_URL, campaign.getId()));
    }

}
