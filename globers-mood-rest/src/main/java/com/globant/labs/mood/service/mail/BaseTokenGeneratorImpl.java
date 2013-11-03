package com.globant.labs.mood.service.mail;

import org.springframework.stereotype.Component;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
public abstract class BaseTokenGeneratorImpl implements TokenGenerator {

    private final String secret;

    /**
     *
     * @param secret
     */
    public BaseTokenGeneratorImpl(final String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }

    @Override
    public String getToken() {
        throw new UnsupportedOperationException();
    }
}
