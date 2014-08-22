package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.exception.BusinessException;
import com.globant.labs.mood.model.persistent.Preference;
import com.globant.labs.mood.model.persistent.PreferenceKey;
import com.globant.labs.mood.repository.data.PreferenceRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.PreferenceService;
import com.google.appengine.api.search.checkers.Preconditions;
import com.google.appengine.repackaged.com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

import static com.globant.labs.mood.exception.BusinessException.ErrorCode.EXPECTATION_FAILED;
import static com.globant.labs.mood.support.StringSupport.on;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Service
public class PreferenceServiceImpl extends AbstractService implements PreferenceService {

    private static final Logger logger = LoggerFactory.getLogger(PreferenceServiceImpl.class);

    @Inject
    private PreferenceRepository preferenceRepository;

    @Transactional(readOnly = true)
    @Override
    public Set<Preference> preferences() {
        logger.info("method=preferences()");
        return new HashSet<Preference>(preferenceRepository.findAll());
    }

    @Override
    public Page<Preference> preferences(final Pageable pageable) {
        logger.info("method=preferences(), args=[pageable=[{}]]", pageable);
        return preferenceRepository.findAll(pageable);
    }

    @Transactional(readOnly = false)
    @Override
    public Preference store(final Preference preference) {
        Preconditions.checkNotNull(preference, "preference is null");

        logger.info("method=preferences(), args=[preference=[{}]]", preference);

        final Preference storedPreference = this.preferenceRepository.findByPreferenceKey(preference.getPreferenceKey());
        if (storedPreference != null) {
            logger.error("method=preferences() - preference key=[{}] already exist", preference.getPreferenceKey());
            throw new BusinessException(on("Preference with key=[{}] already exist.", preference.getPreferenceKey()), EXPECTATION_FAILED);
        }
        return preferenceRepository.save(preference);
    }

    @Transactional(readOnly = false)
    @Override
    public Preference update(final PreferenceKey preferenceKey, final String value) {
        Preconditions.checkNotNull(preferenceKey, "preferenceKey is null");
        Preconditions.checkNotNull(value, "value is null");

        logger.info("method=update(), args=[preferenceKey=[{}], value=[{}]]", preferenceKey, value);

        Preference preference = preferenceRepository.findByPreferenceKey(preferenceKey.getValue());
        if (null != preference) {
            logger.info("method=update() - preferenceKey=[{}] existent, updating with value=[{}]]", preferenceKey, value);
            preference.setPreferenceValue(value);
        } else {
            logger.info("method=update() - preferenceKey=[{}] inexistent, creating preference with value=[{}]]", preferenceKey, value);
            preference = new Preference(preferenceKey, value);
        }
        return preferenceRepository.saveAndFlush(preference);
    }

    @Transactional(readOnly = true)
    @Override
    public Preference preference(final Long preferenceId) {
        Preconditions.checkNotNull(preferenceId, "preferenceId is null");

        logger.info("method=preference(), args=[preferenceId=[{}]]", preferenceId);

        return preferenceRepository.findOne(preferenceId);
    }

    @Transactional(readOnly = true)
    @Override
    public <T> T preference(final String preferenceKey, final Class<T> type) {
        Preconditions.checkNotNull(preferenceKey, "preferenceKey is null");
        Preconditions.checkNotNull(type, "type is null");

        logger.info("method=preference(), args=[preferenceKey=[{}], type=[{}]]", preferenceKey, type);

        final Preference preference = preferenceRepository.findByPreferenceKey(preferenceKey);
        if (null != preference) {
            logger.info("method=preference() - preferenceKey=[{}] found", preferenceKey);
            return (T) preference.getPreferenceValue();
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public String preference(final String preferenceKey) {
        Preconditions.checkNotNull(preferenceKey, "preferenceKey is null");

        logger.info("method=preference(), args=[preferenceKey=[{}]]", preferenceKey);

        final Preference preference = preferenceRepository.findByPreferenceKey(preferenceKey);
        if (null != preference) {
            logger.info("method=preference() - preferenceKey=[{}] found", preferenceKey);
            return preference.getPreferenceValue();
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Preference> preferenceByNamespace(final String namespace) {
        Preconditions.checkNotNull(namespace, "namespace is null");

        logger.info("method=preferenceByNamespace(), args=[namespace=[{}]]", namespace);

        return Sets.newHashSet(preferenceRepository.findByNamespaceLike(namespace));
    }

    @Transactional(readOnly = true)
    @Override
    public String preference(final PreferenceKey preferenceKey) {
        Preconditions.checkNotNull(preferenceKey, "preferenceKey is null");

        logger.info("method=preference(), args=[preferenceKey=[{}]]", preferenceKey);

        return preference(preferenceKey.getValue());
    }

    @Transactional(readOnly = true)
    @Override
    public <T> T preference(final PreferenceKey preferenceKey, final Class<T> type) {
        Preconditions.checkNotNull(preferenceKey, "preferenceKey is null");
        Preconditions.checkNotNull(type, "type is null");

        logger.info("method=preference(), args=[preferenceKey=[{}], type=[{}]]", preferenceKey, type);

        return preference(preferenceKey.getValue(), type);
    }
}
