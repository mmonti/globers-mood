package com.globant.labs.mood.exception;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class TechnicalException extends RuntimeException {

    public TechnicalException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TechnicalException(final String message) {
        super(message);
    }

    public TechnicalException(final Throwable cause) {
        super(cause);
    }

}
