package com.globant.labs.mood.config;

import com.globant.labs.mood.repository.data.factory.GenericRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        value = "com.globant.labs.mood.repository",
        repositoryImplementationPostfix = "Impl",
        repositoryFactoryBeanClass = GenericRepositoryFactoryBean.class,
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class RepositoryConfig {

    public static final String PERSISTENT_UNIT = "persistent.unit";

    @Inject
    private Environment environment;

    /**
     * @return
     */
    @Bean
    @Lazy
    public PlatformTransactionManager transactionManager() {
        final AbstractEntityManagerFactoryBean abstractEntityManagerFactoryBean = entityManagerFactory();
        final EntityManagerFactory entityManagerFactory = abstractEntityManagerFactoryBean.getObject();

        final JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);

        return jpaTransactionManager;
    }

    /**
     * @return
     */
    @Bean
    @Lazy
    public LocalEntityManagerFactoryBean entityManagerFactory() {
        final String persistentUnit = environment.getProperty(PERSISTENT_UNIT);
        final LocalEntityManagerFactoryBean factory = new LocalEntityManagerFactoryBean();
        factory.setPersistenceUnitName(persistentUnit);
        return factory;
    }

    /**
     * @return
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

}
