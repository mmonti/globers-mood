package com.globant.labs.mood.service;

import com.globant.labs.mood.model.persistent.Preference;
import com.globant.labs.mood.model.persistent.PreferenceKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * @param pageable
     * @return
     */
    Page<Preference> preferences(final Pageable pageable);

    /**
     * @param preference
     * @return
     */
    Preference store(final Preference preference);

    /**
     * @param preferenceKey
     * @param value
     * @return
     */
    Preference update(final PreferenceKey preferenceKey, final String value);

    /**
     * @param preferenceId
     * @return
     */
    Preference preference(final Long preferenceId);

    /**
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    <T> T preference(final String key, final Class<T> type);

    /**
     * @param key
     * @return
     */
    String preference(final String key);

    /**
     * @param ns
     * @return
     */
    Set<Preference> preferenceByNamespace(final String ns);

    /**
     * @param key
     * @return
     */
    String preference(final PreferenceKey key);

    /**
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    <T> T preference(final PreferenceKey key, final Class<T> type);
}
