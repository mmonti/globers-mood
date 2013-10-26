package com.globant.labs.mood.resources;

import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface StatsResource {

    /**
     * Returns the statistics of all the entities in the dataStore.
     * It just returns what is cached.
     */
    Response statistics();

    /**
     * @param entity
     * @param entry
     * @return
     */
    Response entryStats(final String entity, final String entry);

    /**
     * Generate the statistics and cache in memcache.
     */
    void generate();
}
