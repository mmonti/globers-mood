package com.globant.labs.mood.repository.data.factory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class GenericRepositoryFactoryBean<R extends JpaRepository<M, ID>, M, ID extends Serializable> extends JpaRepositoryFactoryBean<R, M, ID> {

    /**
     *
     */
    public GenericRepositoryFactoryBean() {
    }

    /**
     * @param entityManager
     * @return
     */
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new GenericRepositoryFactory(entityManager);
    }
}
