package com.globant.labs.mood.service;

import com.globant.labs.mood.events.EventPublisher;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.inject.Inject;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
public abstract class AbstractService {

    @Inject
    private EventPublisher eventPublisher;

    /**
     *
     */
    public void publishAfterCommit(final ApplicationEvent event) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                eventPublisher.publish(event);
            }
        });
    }

    protected void publish(final ApplicationEvent applicationEvent) {
        eventPublisher.publish(applicationEvent);
    }

}
