package com.globant.labs.mood.service.mail.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public abstract class AbstractBaseTokenGenerator implements TokenGenerator {

    private static final Logger logger = LoggerFactory.getLogger(AbstractBaseTokenGenerator.class);

    private final String secret;

    /**
     * @param secret
     */
    public AbstractBaseTokenGenerator(final String secret) {
        this.secret = secret;
    }

    /**
     *
     * @return
     */
    public String getSecret() {
        return secret;
    }

}
