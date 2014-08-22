package com.globant.labs.mood.events.listeners;

import com.globant.labs.mood.events.DispatchCampaignEvent;
import com.globant.labs.mood.events.DispatchUserEvent;
import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.User;
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
public class DispatchUserListener extends AbstractEventListener implements ApplicationListener<DispatchUserEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DispatchUserListener.class);

    private static final String DISPATCH_MAIL_MESSAGES_URL = "/rest/api/v1/dispatch/campaign/{}/user/{}";

    /**
     * @param dispatchEvent
     */
    @Override
    public void onApplicationEvent(final DispatchUserEvent dispatchEvent) {
        final Campaign campaign = dispatchEvent.getCampaign();
        final User user = dispatchEvent.getUser();
        logger.info("event - type=[DispatchUserEvent] - campaignId=[{}], userId=[{}]", campaign.getId(), user.getId());

        enqueueTask(DispatchUserEvent.class, POST, on(DISPATCH_MAIL_MESSAGES_URL, campaign.getId(), user.getId()));
    }

}
