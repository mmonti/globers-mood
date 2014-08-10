package com.globant.labs.mood.repository.data.impl;

import com.globant.labs.mood.repository.data.GenericRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@SuppressWarnings("unchecked")
@NoRepositoryBean
public class GenericRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements GenericRepository<T, ID>, Serializable {

    private static final long serialVersionUID = 1L;

    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager em;

    private Class<?> springDataRepositoryInterface;

    public Class<?> getSpringDataRepositoryInterface() {
        return springDataRepositoryInterface;
    }

    public void setSpringDataRepositoryInterface(Class<?> springDataRepositoryInterface) {
        this.springDataRepositoryInterface = springDataRepositoryInterface;
    }

    /**
     * Creates a new {@link SimpleJpaRepository} to manage objects of the given
     * {@link JpaEntityInformation}.
     *
     * @param entityInformation
     * @param entityManager
     */
    public GenericRepositoryImpl(final JpaEntityInformation<T, ?> entityInformation, final EntityManager entityManager, final Class<?> springDataRepositoryInterface) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.em = entityManager;
        this.springDataRepositoryInterface = springDataRepositoryInterface;
    }

    /**
     * Creates a new {@link SimpleJpaRepository} to manage objects of the given
     * domain type.
     *
     * @param domainClass
     * @param em
     */
    public GenericRepositoryImpl(final Class<T> domainClass, final EntityManager em) {
        this(JpaEntityInformationSupport.getMetadata(domainClass, em), em, null);
    }

    /**
     *
     * @param entity
     * @param <S>
     * @return
     */
    public <S extends T> S save(S entity) {
        if (entityInformation.isNew(entity)) {
            em.persist(entity);
            flush();
            return entity;
        }
        entity = this.em.merge(entity);
        flush();
        return entity;
    }

    /**
     *
     * @param entity
     * @return
     */
    public T saveWithoutFlush(T entity) {
        return super.save(entity);
    }

    /**
     *
     * @param entities
     * @return
     */
    public List<T> saveWithoutFlush(final Iterable<? extends T> entities) {
        final List<T> result = new ArrayList<T>();
        if (entities == null) {
            return result;
        }
        for (final T entity : entities) {
            result.add(saveWithoutFlush(entity));
        }
        return result;
    }
}
