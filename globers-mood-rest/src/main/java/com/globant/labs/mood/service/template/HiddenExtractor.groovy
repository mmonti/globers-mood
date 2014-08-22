package com.globant.labs.mood.service.template

import com.globant.labs.mood.model.persistent.ElementMetadata
import com.globant.labs.mood.model.persistent.ElementType
import com.globant.labs.mood.model.persistent.TemplateMetadata
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * Created by mmonti on 8/20/14.
 */
class HiddenExtractor extends AbstractExtractor {

    private static final String CONSTANT_TYPE_HIDDEN = "[type=hidden]";

    @Override
    public void process(final TemplateMetadata metadata, final Document document) {
        document.select(CONSTANT_TYPE_HIDDEN).groupBy({ element -> element.attr(CONSTANT_NAME) })
                .each() { attribute -> metadata.addElementMetadata(new ElementMetadata(attribute.key, ElementType.HIDDEN));
        };
        processNext(metadata, document);
    }

}
