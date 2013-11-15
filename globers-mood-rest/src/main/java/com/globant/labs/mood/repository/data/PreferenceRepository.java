package com.globant.labs.mood.repository.data;

import com.globant.labs.mood.model.persistent.Preference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author mauro.monti (mauro.monti@globant.com)
 */
public interface PreferenceRepository extends GenericRepository<Preference, Long> {

    /**
     *
     * @param preferenceKey
     * @return
     */
    Preference findByPreferenceKey(final String preferenceKey);

    /**
     *
     * @param namespace
     * @return
     */
    Preference findByNamespace(final String namespace);

}