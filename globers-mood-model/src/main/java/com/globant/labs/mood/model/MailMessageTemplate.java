package com.globant.labs.mood.model;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface MailMessageTemplate {

    /**
     *
     * @return
     */
    String eval(final Object context);

}