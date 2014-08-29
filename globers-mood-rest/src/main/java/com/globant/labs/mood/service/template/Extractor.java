package com.globant.labs.mood.service.template;

import com.globant.labs.mood.model.persistent.TemplateMetadata;

import org.jsoup.nodes.Document;

/**
 * Created by mmonti on 8/20/14.
 */
public interface Extractor {

    /**
     * @param processorHandler
     */
    void setNext(final Extractor processorHandler);

    /**
     * @param metadata
     * @param document
     */
    void process(final TemplateMetadata metadata, final Document document);

}
