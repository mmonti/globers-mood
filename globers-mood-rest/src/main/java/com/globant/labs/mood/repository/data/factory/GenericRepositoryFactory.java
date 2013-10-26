package com.globant.labs.mood.repository.data.factory;

import com.globant.labs.mood.repository.data.GenericRepository;
import com.globant.labs.mood.repository.data.impl.GenericRepositoryImpl;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 *
 * @param <M>
 * @param <ID>
 */
public class GenericRepositoryFactory<M, ID extends Serializable> extends JpaRepositoryFactory {

    private EntityManager entityManager;

    public GenericRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    protected Object getTargetRepository(RepositoryMetadata metadata) {
        final Class<?> repositoryInterface = metadata.getRepositoryInterface();
        if (isBaseRepository(repositoryInterface)) {
            return new GenericRepositoryImpl<M, ID>((Class<M>) metadata.getDomainType(), entityManager);
        }
        return super.getTargetRepository(metadata);
    }

    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        if (isBaseRepository(metadata.getRepositoryInterface())) {
            return GenericRepository.class;
        }
        return super.getRepositoryBaseClass(metadata);
    }

    private boolean isBaseRepository(Class<?> repositoryInterface) {
        return GenericRepository.class.isAssignableFrom(repositoryInterface);
    }

    @Override
    protected QueryLookupStrategy getQueryLookupStrategy(QueryLookupStrategy.Key key) {
        return super.getQueryLookupStrategy(key);
    }
}