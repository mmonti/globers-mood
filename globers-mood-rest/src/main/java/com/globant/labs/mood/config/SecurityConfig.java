package com.globant.labs.mood.config;

import com.globant.labs.mood.security.TokenCheckFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.inject.Inject;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Configuration
public class SecurityConfig {

    private static final String API_TOKEN = "api.token";

    @Inject
    private Environment environment;

    @Bean
    public TokenCheckFilter tokenCheckFilter() {
        final String tokenValue = environment.getProperty(API_TOKEN);
        return new TokenCheckFilter(tokenValue);
    }

}
