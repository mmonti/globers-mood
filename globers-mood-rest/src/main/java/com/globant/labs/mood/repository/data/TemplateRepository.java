package com.globant.labs.mood.repository.data;

import com.globant.labs.mood.model.persistent.Template;

/**
 * @author mauro.monti (mauro.monti@globant.com)
 */
public interface TemplateRepository extends GenericRepository<Template, Long> {

    /**
     * @param name
     * @return
     */
    Template findByName(final String name);

}