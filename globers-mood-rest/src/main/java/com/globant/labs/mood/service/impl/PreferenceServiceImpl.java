package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.model.persistent.Preference;
import com.globant.labs.mood.model.persistent.PreferenceKey;
import com.globant.labs.mood.repository.data.PreferenceRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.PreferenceService;
import com.google.appengine.api.search.checkers.Preconditions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Service
public class PreferenceServiceImpl extends AbstractService implements PreferenceService {

    @Inject
    private PreferenceRepository preferenceRepository;

    @Transactional(readOnly = true)
    @Override
    public Set<Preference> preferences() {
        return new HashSet<Preference>(preferenceRepository.findAll());
    }

    @Transactional(readOnly = false)
    @Override
    public Preference store(final Preference preference) {
        return preferenceRepository.save(preference);
    }

    @Transactional(readOnly = false)
    @Override
    public Preference update(final PreferenceKey preferenceKey, final String value) {
        Preconditions.checkNotNull(value, "value is null");
        Preference preference = preferenceRepository.findByPreferenceKey(preferenceKey.getValue());
        if (null != preference) {
            preference.setPreferenceValue(value);
        } else {
            preference = new Preference(preferenceKey, value);
        }
        return store(preference);
    }

    @Transactional(readOnly = true)
    @Override
    public Preference preference(long id) {
        return preferenceRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @Override
    public <T> T preference(final String key, final Class<T> type) {
        final Preference preference = preferenceRepository.findByPreferenceKey(key);
        if (null != preference) {
            return (T) preference.getPreferenceValue();
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public String preference(final String key) {
        final Preference preference = preferenceRepository.findByPreferenceKey(key);
        if (null != preference) {
            return preference.getPreferenceValue();
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public String preference(final PreferenceKey key) {
        return preference(key.getValue());
    }

    @Transactional(readOnly = true)
    @Override
    public <T> T preference(final PreferenceKey key, final Class<T> type) {
        return preference(key.getValue(), type);
    }
}
