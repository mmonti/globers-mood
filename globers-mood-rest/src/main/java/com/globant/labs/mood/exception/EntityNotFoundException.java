package com.globant.labs.mood.exception;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(final Class type, final Object value) {
        this("Entity=["+type.getCanonicalName()+"] with attribute value=["+value+"] not found.");
    }
}
