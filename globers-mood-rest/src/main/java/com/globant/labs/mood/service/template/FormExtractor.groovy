package com.globant.labs.mood.service.template

import com.globant.labs.mood.model.persistent.ElementMetadata
import com.globant.labs.mood.model.persistent.ElementType
import com.globant.labs.mood.model.persistent.TemplateMetadata
import org.jsoup.nodes.Document

/**
 * Created by mmonti on 8/20/14.
 */
class FormExtractor extends AbstractExtractor {

    private static final String CONSTANT_FORM = "form";

    @Override
    public void process(final TemplateMetadata collector, final Document document) {
        document.select(CONSTANT_FORM).groupBy( {
            element -> element.attr(CONSTANT_NAME)
        }).each() { attribute ->
            if (!hasValidKey(attribute.key)) {
                return
            }
            def elementMetadata = new ElementMetadata(attribute.key, ElementType.FORM)
            elementMetadata.addAttribute(CONSTANT_ACTION, attribute.value.first().attr(CONSTANT_ACTION))

            register(collector, elementMetadata)
        };
        processNext(collector, document);
    }

}
