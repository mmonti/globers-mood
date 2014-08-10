package com.globant.labs.mood.service.mail.template.handlebars;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Template;
import com.globant.labs.mood.exception.TechnicalException;
import com.globant.labs.mood.model.mail.MailMessageTemplate;

import java.io.IOException;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class HBMMailMessageTemplate implements MailMessageTemplate {

    private Template template;

    public HBMMailMessageTemplate(final Template template) {
        this.template = template;
    }

    @Override
    public String eval(final Object context) {
        final Context hbmContext = Context.newContext(context);
        try {
            return this.template.apply(hbmContext);
        } catch (IOException e) {
            throw new TechnicalException("applying context to template.", e);
        }
    }
}
