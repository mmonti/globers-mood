package com.globant.labs.mood.service.template;

import com.globant.labs.mood.model.persistent.Template;
import com.globant.labs.mood.model.persistent.TemplateMetadata;

/**
 * Created by mmonti on 8/20/14.
 */
public interface TemplateProcessor {

    /**
     * @param template
     */
    TemplateMetadata extract(final Template template);

    /**
     * @param extractor
     */
    void addExtractor(final Extractor extractor);

}
