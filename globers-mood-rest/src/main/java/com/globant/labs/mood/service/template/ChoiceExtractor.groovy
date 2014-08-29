package com.globant.labs.mood.service.template

import com.globant.labs.mood.model.persistent.Attribute
import com.globant.labs.mood.model.persistent.ElementMetadata
import com.globant.labs.mood.model.persistent.ElementType
import com.globant.labs.mood.model.persistent.TemplateMetadata
import org.jsoup.nodes.Document

/**
 * Created by mmonti on 8/20/14.
 */
class ChoiceExtractor extends AbstractExtractor {

    private static final String CONSTANT_TYPE_RADIO = "[type=radio]";

    private static final String CONSTANT_NUMBER = Double.class.getCanonicalName();
    private static final String CONSTANT_TEXT = String.class.getCanonicalName();

    @Override
    public void process(final TemplateMetadata collector, final Document document) {
        document.select(CONSTANT_TYPE_RADIO).groupBy(

                { element -> element.attr(CONSTANT_NAME) },
                { element -> element.attr(CONSTANT_VALUE) }

        ).each() {

            attribute ->
                if (!hasValidKey(attribute.key)) {
                    return
                }

                def values = attribute.getValue().collect { item -> item.key } as List<String>
                def elementMetadata = new ElementMetadata(attribute.key, ElementType.CHOICE)
                elementMetadata.setValueType(values.first().isNumber() ? CONSTANT_NUMBER : CONSTANT_TEXT)
                elementMetadata.addValues(values)

                register(collector, elementMetadata)
        };
        processNext(collector, document);
    }

}
