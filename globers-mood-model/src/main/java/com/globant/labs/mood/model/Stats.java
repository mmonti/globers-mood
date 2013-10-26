package com.globant.labs.mood.model;

import com.google.appengine.repackaged.com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class Stats implements Serializable {

    private Map<StatsEntry, Node> entries;

    /**
     *
     */
    public Stats() {
        this.entries = new ConcurrentHashMap<StatsEntry, Node>();
    }

    /**
     *
     * @return
     */
    public Map<StatsEntry, Node> getStats() {
        return entries;
    }

    /**
     *
     * @param statsEntry
     * @return
     */
    public Node getEntry(final StatsEntry statsEntry) {
        Preconditions.checkNotNull(statsEntry, "statsEntry cannot be null");

        return entries.get(statsEntry);
    }

    /**
     *
     * @param statsEntry
     * @param node
     */
    public void addStat(final StatsEntry statsEntry, final Node node) {
        Preconditions.checkNotNull(statsEntry, "statsEntry cannot be null");
        Preconditions.checkNotNull(node, "node cannot be null");

        this.entries.put(statsEntry, node);
    }

}
