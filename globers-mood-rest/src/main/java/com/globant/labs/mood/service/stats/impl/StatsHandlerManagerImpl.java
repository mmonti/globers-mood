package com.globant.labs.mood.service.stats.impl;

import com.globant.labs.mood.model.Node;
import com.globant.labs.mood.model.Stats;
import com.globant.labs.mood.model.StatsEntry;
import com.globant.labs.mood.service.cache.CacheService;
import com.globant.labs.mood.service.stats.StatsEntryHandler;
import com.globant.labs.mood.service.stats.StatsHandlerManager;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
public class StatsHandlerManagerImpl implements StatsHandlerManager {

    @Inject
    private CacheService cacheService;
    private Map<StatsEntry, StatsEntryHandler> handlerMap;

    public StatsHandlerManagerImpl() {
        this.handlerMap = new ConcurrentHashMap<StatsEntry, StatsEntryHandler>();
    }

    @Override
    public void register(final StatsEntry statsEntry, final StatsEntryHandler handler) {
        Preconditions.checkNotNull(statsEntry, "statsEntry cannot be null");
        Preconditions.checkNotNull(handler, "handler cannot be null");

        if (!this.handlerMap.containsKey(statsEntry)) {
            this.handlerMap.put(statsEntry, handler);
        }
    }

    @Override
    public void execute(final StatsEntry statsEntry) {
        Preconditions.checkNotNull(statsEntry, "statsEntry cannot be null");
        execute(statsEntry);
    }

    @Override
    public void execute(final StatsEntry statsEntry, final Object entity) {
        Preconditions.checkNotNull(statsEntry, "statsEntry cannot be null");
        Preconditions.checkNotNull(entity, "entity cannot be null");

        final StatsEntryHandler statsEntryHandler = this.handlerMap.get(statsEntry);
        if (statsEntryHandler != null) {
            addStatEntry(statsEntry, statsEntryHandler.handle(statsEntry, entity));
        }
    }

    /**
     *
     * @param statsEntry
     * @param node
     */
    public void addStatEntry(final StatsEntry statsEntry, final Node node) {
        Preconditions.checkNotNull(statsEntry, "statsEntry cannot be null");
        Preconditions.checkNotNull(node, "node cannot be null");

        // = Add to the cache.
        cacheService.store(statsEntry, node);
    }

    @Override
    public Stats getStats() {
        final Stats stats = new Stats();
        for (final StatsEntry currentEntry : StatsEntry.values()) {
            final Node entry = cacheService.get(currentEntry, Node.class);
            if (null != entry) {
                stats.addStat(currentEntry, entry);
            }
        }
        return stats;
    }
}
