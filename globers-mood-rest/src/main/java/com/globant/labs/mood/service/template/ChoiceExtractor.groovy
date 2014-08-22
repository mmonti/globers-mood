package com.globant.labs.mood.service.template

import com.globant.labs.mood.model.persistent.ElementMetadata
import com.globant.labs.mood.model.persistent.ElementType
import com.globant.labs.mood.model.persistent.TemplateMetadata
import org.jsoup.nodes.Document

/**
 * Created by mmonti on 8/20/14.
 */
class ChoiceExtractor extends AbstractExtractor {

    private static final String CONSTANT_TYPE_RADIO = "[type=radio]";

    @Override
    public void process(final TemplateMetadata metadata, final Document document) {
        document.select(CONSTANT_TYPE_RADIO).groupBy(
                { element -> element.attr(CONSTANT_NAME) },
                { element -> element.attr(CONSTANT_VALUE) }
        ).each() { attribute ->
            metadata.addElementMetadata(new ElementMetadata(attribute.key, ElementType.CHOICE));
        };
        processNext(metadata, document);
    }

}
