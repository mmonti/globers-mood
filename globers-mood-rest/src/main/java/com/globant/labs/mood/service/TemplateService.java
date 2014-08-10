package com.globant.labs.mood.service;

import com.globant.labs.mood.model.persistent.Template;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.InputStream;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface TemplateService {

    /**
     * @return
     */
    Page<Template> templates(final Pageable pageable);

    /**
     * @param template
     * @return
     */
    Template store(final Template template);

    /**
     * @param name
     * @param inputStream
     * @return
     */
    Template store(final String name, final InputStream inputStream);

    /**
     * @param id
     * @return
     */
    Template template(final long id);

    /**
     * @param name
     * @return
     */
    Template templateByName(final String name);

}
