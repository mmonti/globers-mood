package com.globant.labs.mood.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Produces(MediaType.APPLICATION_JSON)
public abstract class AbstractResource {

    private static final Logger logger = LoggerFactory.getLogger(AbstractResource.class);

    /**
     *
     * @param entity
     * @return
     */
    protected Response notNullResponse(final Object entity) {
        if (entity != null) {
            return Response.ok(entity).build();
        }
        return noContent();
    }

    /**
     *
     * @param entityList
     * @param <T>
     * @return
     */
    protected <T> Response notEmptyResponse(final Collection<T> entityList) {
        if (entityList != null && !entityList.isEmpty()) {
            return Response.ok(entityList).build();
        }
        return noContent();
    }

    /**
     *
     * @param iterable
     * @param <T>
     * @return
     */
    protected <T> Response notEmptyResponse(final Iterable<T> iterable) {
        if (iterable != null && iterable.iterator().hasNext()) {
            return Response.ok(iterable).build();
        }
        return noContent();
    }

    /**
     *
     * @return
     */
    protected Response noContent() {
        return Response.noContent().build();
    }
}
