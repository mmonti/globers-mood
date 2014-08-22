package com.globant.labs.mood.repository.data;

import com.globant.labs.mood.model.persistent.Template;
import org.springframework.data.jpa.repository.Query;

/**
 * @author mauro.monti (mauro.monti@globant.com)
 */
public interface TemplateRepository extends GenericRepository<Template, Long> {

    /**
     * @param name
     * @return
     */
    Template findByName(final String name);

    /**
     * @param templateId
     * @return
     */
    @Query("select count(template) from Template template where template.id = ?1")
    Long count(final Long templateId);
}