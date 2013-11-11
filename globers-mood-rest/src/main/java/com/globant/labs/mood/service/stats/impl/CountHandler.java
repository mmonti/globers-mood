package com.globant.labs.mood.service.stats.impl;

import com.globant.labs.mood.model.Measure;
import com.globant.labs.mood.model.Node;
import com.globant.labs.mood.model.NodeBuilder;
import com.globant.labs.mood.model.StatsEntry;
import com.globant.labs.mood.repository.data.GenericRepository;
import com.globant.labs.mood.service.stats.AbstractStatsHandler;
import com.globant.labs.mood.service.stats.StatsEntryHandler;
import com.google.appengine.api.search.checkers.Preconditions;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class CountHandler extends AbstractStatsHandler implements StatsEntryHandler {

    private GenericRepository genericRepository;

    /**
     *
     * @param genericRepository
     */
    public CountHandler(final GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    @Override
    public Node handle(final StatsEntry statsEntry, final Object entity) {
        Preconditions.checkNotNull(statsEntry, "statsEntry cannot be null");
        Preconditions.checkNotNull(entity, "entity cannot be null");

        final Class type = Class.class.cast(entity);
        final long count = genericRepository.count();

        return NodeBuilder.create(type.getCanonicalName()).path(Measure.COUNT).value(count);
    }

}
