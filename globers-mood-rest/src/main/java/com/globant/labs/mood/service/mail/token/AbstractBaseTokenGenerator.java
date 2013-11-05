package com.globant.labs.mood.service.mail.token;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public abstract class AbstractBaseTokenGenerator implements TokenGenerator {

    private final String secret;

    /**
     *
     * @param secret
     */
    public AbstractBaseTokenGenerator(final String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }

}
