package com.globant.labs.mood.repository.data;

import com.globant.labs.mood.model.persistent.Template;
import com.globant.labs.mood.model.persistent.TemplateMetadata;
import org.springframework.data.jpa.repository.Query;

/**
 * @author mauro.monti (mauro.monti@globant.com)
 */
public interface TemplateMetadataRepository extends GenericRepository<TemplateMetadata, Long> {

}