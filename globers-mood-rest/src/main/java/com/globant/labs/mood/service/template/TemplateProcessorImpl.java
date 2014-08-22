package com.globant.labs.mood.service.template;

import com.globant.labs.mood.model.persistent.Template;
import com.globant.labs.mood.model.persistent.TemplateMetadata;
import com.google.common.base.Preconditions;

import static org.jsoup.Jsoup.parse;

/**
 * Created by mmonti on 8/20/14.
 */
public class TemplateProcessorImpl implements TemplateProcessor {

    private Extractor extractor;

    /**
     * @param template
     * @return
     */
    public TemplateMetadata extract(final Template template) {
        Preconditions.checkNotNull(template, "template is null");

        final String templateContent = template.getTemplateContent();
        if (templateContent == null) {
            return null;
        }

        final TemplateMetadata metadata = new TemplateMetadata();
        extractor.process(metadata, parse(templateContent).normalise());

        return metadata;
    }

    /**
     * @param extractor
     */
    public void addExtractor(final Extractor extractor) {
        extractor.setNext(this.extractor);
        this.extractor = extractor;
    }

}
