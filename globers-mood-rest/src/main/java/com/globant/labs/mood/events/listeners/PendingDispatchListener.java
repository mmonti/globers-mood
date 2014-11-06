package com.globant.labs.mood.events.listeners;

import com.globant.labs.mood.events.PendingDispatchEvent;
import com.globant.labs.mood.model.mail.DispatchResult;
import com.globant.labs.mood.model.mail.MailMessage;
import com.globant.labs.mood.model.persistent.PendingMail;
import com.globant.labs.mood.service.PendingMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Set;

/**
 * Created by mmonti on 8/13/14.
 */
@Component
public class PendingDispatchListener extends AbstractEventListener implements ApplicationListener<PendingDispatchEvent> {

    private static final Logger logger = LoggerFactory.getLogger(PendingDispatchListener.class);

    @Inject
    private PendingMailService pendingMailService;

    /**
     * @param pendingDispatchEvent
     */
    @Override
    public void onApplicationEvent(final PendingDispatchEvent pendingDispatchEvent) {
        final DispatchResult dispatchResult = pendingDispatchEvent.getDispatchResult();
        final Set<MailMessage> pendings = dispatchResult.getDispatchPending();
        for (MailMessage mailMessage : pendings) {
            final PendingMail pendingMail = new PendingMail(mailMessage.getCampaign().getName(), mailMessage.getTarget().getEmail());
            pendingMailService.store(pendingMail);
        }
    }
}
