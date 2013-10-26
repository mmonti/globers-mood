package com.globant.labs.mood.events;

import com.globant.labs.mood.model.StatsEntry;
import com.globant.labs.mood.service.StatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
public class StatsEventListener implements ApplicationListener<StatsEvent> {

    private static final Logger logger = LoggerFactory.getLogger(StatsEventListener.class);

    @Inject
    private StatsService statsService;

    @Override
    public void onApplicationEvent(final StatsEvent event) {
        final Object eventSource = event.getSource();
        final Class eventType = event.getType();
        final StatsEntry statEntry = event.getEntry();

        logger.debug("Event arrived source=[{}], type=[{}], entry=[{}]", eventSource, eventType, statEntry);

        statsService.executeStatEntryHandler(statEntry, eventType);
    }
}
