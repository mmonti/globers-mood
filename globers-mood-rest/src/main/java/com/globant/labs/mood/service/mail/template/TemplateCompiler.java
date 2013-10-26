package com.globant.labs.mood.service.mail.template;

import com.globant.labs.mood.model.MailMessageTemplate;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface TemplateCompiler {

    /**
     *
     * @param name
     * @return
     */
    MailMessageTemplate compile(final String name);
}
