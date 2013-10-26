package com.globant.labs.mood.service;

import com.globant.labs.mood.model.Node;
import com.globant.labs.mood.model.Stats;
import com.globant.labs.mood.model.StatsEntry;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface StatsService {

    /**
     *
     * @param statsEntry
     */
    void executeStatEntryHandler(final StatsEntry statsEntry);

    /**
     *
     * @param statsEntry
     * @param entity
     */
    <T> void executeStatEntryHandler(final StatsEntry statsEntry, Class<T> entity);

    /**
     *
     * @return
     */
    Stats getStats();

    /**
     *
     * @param statsEntry
     * @return
     */
    Node getStatEntry(final StatsEntry statsEntry);
}
