package com.globant.labs.mood.service.template;

import com.globant.labs.mood.model.persistent.ElementMetadata;
import com.globant.labs.mood.model.persistent.ElementType;
import com.globant.labs.mood.model.persistent.TemplateMetadata;
import com.google.common.collect.ArrayListMultimap;
import org.jsoup.nodes.Document;

/**
 * Created by mmonti on 8/20/14.
 */
public abstract class AbstractExtractor implements Extractor {

    protected static final String CONSTANT_ACTION = "action";
    protected static final String CONSTANT_NAME = "name";
    protected static final String CONSTANT_VALUE = "value";

    private Extractor next;

    @Override
    public void setNext(final Extractor processorHandler) {
        if (next == null) {
            next = processorHandler;
        } else {
            next.setNext(processorHandler);
        }
    }

    protected void processNext(final TemplateMetadata metadata, final Document document) {
        if (next != null) {
            next.process(metadata, document);
        }
    }

    protected void register(final TemplateMetadata collector, final ElementMetadata metadata) {
        collector.addElementMetadata(metadata);
    }

    protected boolean hasValidKey(final String key) {
        return (key != null && !key.isEmpty());
    }

    @Override
    public abstract void process(final TemplateMetadata metadata, final Document document);

}
