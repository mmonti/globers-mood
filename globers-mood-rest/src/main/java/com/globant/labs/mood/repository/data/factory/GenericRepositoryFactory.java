package com.globant.labs.mood.repository.data.factory;

import com.globant.labs.mood.repository.data.GenericRepository;
import com.globant.labs.mood.repository.data.impl.GenericRepositoryImpl;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * @param <M>
 * @param <ID>
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class GenericRepositoryFactory<M, ID extends Serializable> extends JpaRepositoryFactory {

    private EntityManager entityManager;

    /**
     * @param entityManager
     */
    public GenericRepositoryFactory(final EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    /**
     * @param metadata
     * @return
     */
    protected Object getTargetRepository(final RepositoryMetadata metadata) {
        final Class<?> repositoryInterface = metadata.getRepositoryInterface();
        if (isBaseRepository(repositoryInterface)) {
            return new GenericRepositoryImpl<M, ID>((Class<M>) metadata.getDomainType(), entityManager);
        }
        return super.getTargetRepository(metadata);
    }

    /**
     * @param metadata
     * @return
     */
    protected Class<?> getRepositoryBaseClass(final RepositoryMetadata metadata) {
        if (isBaseRepository(metadata.getRepositoryInterface())) {
            return GenericRepository.class;
        }
        return super.getRepositoryBaseClass(metadata);
    }

    /**
     * @param repositoryInterface
     * @return
     */
    private boolean isBaseRepository(final Class<?> repositoryInterface) {
        return GenericRepository.class.isAssignableFrom(repositoryInterface);
    }

    /**
     * @param key
     * @return
     */
    @Override
    protected QueryLookupStrategy getQueryLookupStrategy(final QueryLookupStrategy.Key key) {
        return super.getQueryLookupStrategy(key);
    }
}