package com.globant.labs.mood.events.listeners;

import com.globant.labs.mood.events.PendingDispatchEvent;
import com.globant.labs.mood.model.mail.DispatchResult;
import com.globant.labs.mood.model.mail.MailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created by mmonti on 8/13/14.
 */
@Component
public class PendingDispatchListener extends AbstractEventListener implements ApplicationListener<PendingDispatchEvent> {

    private static final Logger logger = LoggerFactory.getLogger(PendingDispatchListener.class);

    /**
     * @param pendingDispatchEvent
     */
    @Override
    public void onApplicationEvent(final PendingDispatchEvent pendingDispatchEvent) {
        final DispatchResult dispatchResult = pendingDispatchEvent.getDispatchResult();
        final Set<MailMessage> pendings = dispatchResult.getDispatchPending();
    }
}
