package com.globant.labs.mood.service.stats;

import com.globant.labs.mood.model.Node;
import com.globant.labs.mood.model.StatsEntry;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface StatsEntryHandler {

    /**
     *
     */
    Node handle(final StatsEntry statsEntry, final Object entity);
}
