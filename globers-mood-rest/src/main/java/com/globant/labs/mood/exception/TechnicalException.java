package com.globant.labs.mood.exception;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class TechnicalException extends RuntimeException {

    /**
     *
     * @param message
     * @param cause
     */
    public TechnicalException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param message
     */
    public TechnicalException(final String message) {
        super(message);
    }

    /**
     *
     * @param cause
     */
    public TechnicalException(final Throwable cause) {
        super(cause);
    }

}
