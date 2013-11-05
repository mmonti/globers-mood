package com.globant.labs.mood.exception;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(final String receivedToken, final String generatedToken) {
        super("received token=["+receivedToken+"] must match with the generated token=["+generatedToken);
    }

}
