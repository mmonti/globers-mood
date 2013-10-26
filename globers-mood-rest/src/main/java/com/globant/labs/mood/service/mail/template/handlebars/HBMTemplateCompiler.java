package com.globant.labs.mood.service.mail.template.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.globant.labs.mood.exception.ServiceException;
import com.globant.labs.mood.model.MailMessageTemplate;
import com.globant.labs.mood.service.mail.template.TemplateCompiler;
import com.google.appengine.repackaged.com.google.common.base.Preconditions;

import java.io.IOException;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class HBMTemplateCompiler implements TemplateCompiler {

    private Handlebars handlebars;

    public HBMTemplateCompiler(final TemplateLoader templateLoader) {
        this.handlebars = new Handlebars(templateLoader);
    }

    @Override
    public MailMessageTemplate compile(final String name) {
        Preconditions.checkNotNull(name, "name is null");
        try {
            return new HBMMailMessageTemplate(handlebars.compile(name));

        } catch (IOException e) {
            throw new ServiceException("");
        }
    }
}
