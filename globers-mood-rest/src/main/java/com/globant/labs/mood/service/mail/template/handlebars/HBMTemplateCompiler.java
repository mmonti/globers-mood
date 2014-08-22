package com.globant.labs.mood.service.mail.template.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.globant.labs.mood.exception.TechnicalException;
import com.globant.labs.mood.model.mail.MailMessageTemplate;
import com.globant.labs.mood.service.mail.template.TemplateCompiler;
import com.google.appengine.repackaged.com.google.common.base.Preconditions;

import java.io.IOException;

import static com.globant.labs.mood.support.StringSupport.on;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class HBMTemplateCompiler implements TemplateCompiler {

    private Handlebars handlebars;

    public HBMTemplateCompiler(final TemplateLoader templateLoader) {
        this.handlebars = new Handlebars(templateLoader);
    }

    /**
     * @param helperSource
     * @return
     */
    public HBMTemplateCompiler register(final Class<?> helperSource) {
        this.handlebars.registerHelpers(helperSource);
        return this;
    }

    /**
     * @param name
     * @param helper
     * @param <T>
     * @return
     */
    public <T> HBMTemplateCompiler register(final String name, final Helper<T> helper) {
        this.handlebars.registerHelper(name, helper);
        return this;
    }

    @Override
    public MailMessageTemplate compile(final String name) {
        Preconditions.checkNotNull(name, "name is null");
        try {
            return new HBMMailMessageTemplate(handlebars.compile(name));

        } catch (IOException e) {
            throw new TechnicalException(on("unable to compile template with name=[{}]", name), e);
        }
    }
}
