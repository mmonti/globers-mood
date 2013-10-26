package com.globant.labs.mood.service;

import com.globant.labs.mood.model.persistent.Preference;
import com.globant.labs.mood.model.persistent.PreferenceKey;

import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface PreferenceService {

    /**
     * @return
     */
    Set<Preference> preferences();

    /**
     *
     * @param preference
     * @return
     */
    Preference store(final Preference preference);

    /**
     *
     * @param preferenceKey
     * @param value
     * @return
     */
    Preference update(final PreferenceKey preferenceKey, String value);

    /**
     * @param id
     * @return
     */
    Preference preference(final long id);

    /**
     *
     * @param key
     * @return
     */
    <T> T preference(final String key, Class<T> type);

    /**
     *
     * @param key
     * @return
     */
    String preference(final String key);

    /**
     *
     * @param key
     * @return
     */
    String preference(final PreferenceKey key);

    /**
     *
     * @param key
     * @return
     */
    <T> T preference(final PreferenceKey key, Class<T> type);
}
