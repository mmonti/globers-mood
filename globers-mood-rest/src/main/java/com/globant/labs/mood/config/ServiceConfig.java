package com.globant.labs.mood.config;

import com.github.jknack.handlebars.io.TemplateLoader;
import com.globant.labs.mood.service.PreferenceService;
import com.globant.labs.mood.service.TemplateService;
import com.globant.labs.mood.service.mail.MailMessageFactory;
import com.globant.labs.mood.service.mail.template.TemplateCompiler;
import com.globant.labs.mood.service.mail.template.handlebars.HBMTemplateCompiler;
import com.globant.labs.mood.service.mail.template.handlebars.RepositoryTemplateLoader;
import com.globant.labs.mood.service.mail.token.TokenGenerator;
import com.globant.labs.mood.service.mail.token.impl.UserTokenGeneratorImpl;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import javax.mail.Session;
import java.util.Properties;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Configuration
@ComponentScan(basePackages = {"com.globant.labs.mood.service..*"})
public class ServiceConfig {

    public static final String MAIL_TOKEN_SECRET = "mail.token.secret";

    @Inject
    private Environment environment;

    @Inject
    private TemplateService templateService;

    @Inject
    private PreferenceService preferenceService;

    @Bean
    public MemcacheService memcacheService() {
        return MemcacheServiceFactory.getMemcacheService();
    }

    @Bean
    public MailMessageFactory mailMessageBuilder() {
        return new MailMessageFactory(tokenGenerator(), preferenceService, templateCompiler());
    }

    @Bean
    public TemplateCompiler templateCompiler() {
        return new HBMTemplateCompiler(repositoryTemplateLoader());
    }

    @Bean
    public TemplateLoader repositoryTemplateLoader() {
        return new RepositoryTemplateLoader(templateService);
    }

    @Bean
    public Session session() {
        return Session.getDefaultInstance(new Properties(), null);
    }

    @Bean
    public TokenGenerator tokenGenerator() {
        final String secret = environment.getProperty(MAIL_TOKEN_SECRET);
        return new UserTokenGeneratorImpl(secret);
    }

}
