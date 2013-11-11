package com.globant.labs.mood.exception;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class EntityAlreadyExistentException extends RuntimeException {

    public EntityAlreadyExistentException(final String message) {
        super(message);
    }

    public EntityAlreadyExistentException(final Class type, final Object value) {
        this("Entity=["+type.getCanonicalName()+"] with attribute value=["+value+"] already exist in the datastore.");
    }
}
