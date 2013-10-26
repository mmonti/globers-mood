package com.globant.labs.mood.service.stats;

import com.globant.labs.mood.model.Stats;
import com.globant.labs.mood.model.StatsEntry;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface StatsHandlerManager {

    /**
     * @param statsEntry
     * @param handler
     */
    void register(final StatsEntry statsEntry, final StatsEntryHandler handler);

    /**
     * @param statsEntry
     */
    void execute(final StatsEntry statsEntry);

    /**
     * @param statsEntry
     * @param entity
     */
    void execute(final StatsEntry statsEntry, final Object entity);

    /**
     * @return
     */
    Stats getStats();

}
