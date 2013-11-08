package com.globant.labs.mood.service;

import com.globant.labs.mood.model.persistent.Template;

import java.io.InputStream;
import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface TemplateService {

    /**
     * @return
     */
    Set<Template> templates();

    /**
     *
     * @param template
     * @return
     */
    Template store(final Template template);

    /**
     *
     * @param name
     * @param inputStream
     * @return
     */
    Template store(final String name, final InputStream inputStream);

    /**
     *
     * @param id
     * @return
     */
    Template template(final long id);

    /**
     *
     * @param name
     * @return
     */
    Template templateByName(final String name);

}
