package com.globant.labs.mood.service.mail.template.handlebars;

import com.github.jknack.handlebars.io.AbstractTemplateLoader;
import com.github.jknack.handlebars.io.StringTemplateSource;
import com.github.jknack.handlebars.io.TemplateSource;
import com.globant.labs.mood.model.persistent.Template;
import com.globant.labs.mood.service.TemplateService;

import java.io.IOException;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class RepositoryTemplateLoader extends AbstractTemplateLoader {

    private TemplateService templateService;

    public RepositoryTemplateLoader(final TemplateService templateService) {
        this.templateService = templateService;
    }

    @Override
    public TemplateSource sourceAt(final String location) throws IOException {
        final Template template = templateService.templateByName(location);
        return new StringTemplateSource(template.getName(), template.getTemplate());
    }
}
