package com.globant.labs.mood.model.mail;

import com.globant.labs.mood.model.mail.impl.MapBackedContext;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class ContextFactory {

    /**
     * @return
     */
    public static Context getInstance() {
        return new MapBackedContext();
    }

}
