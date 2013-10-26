package com.globant.labs.mood.resources;

import com.globant.labs.mood.model.persistent.PreferenceKey;
import com.globant.labs.mood.model.persistent.Project;

import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface PreferenceResource {

    /**
     *
     * @param preferenceKey
     * @return
     */
    Response preference(final PreferenceKey preferenceKey);

    /**
     *
     * @param preferenceKey
     * @param value
     * @return
     */
    Response update(final PreferenceKey preferenceKey, final String value);

    /**
     *
     * @return
     */
    Response preferences();
}
