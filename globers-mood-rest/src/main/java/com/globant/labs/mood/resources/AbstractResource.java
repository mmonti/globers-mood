package com.globant.labs.mood.resources;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Produces(MediaType.APPLICATION_JSON)
public abstract class AbstractResource {

    protected Response notNullResponse(Object entity) {
        if (entity != null) {
            return Response.ok(entity).build();
        }
        return noContent();
    }

    protected <T> Response notEmptyResponse(Collection<T> entityList) {
        if (entityList != null && !entityList.isEmpty()) {
            return Response.ok(entityList).build();
        }
        return noContent();
    }

    protected Response noContent() {
        return Response.noContent().build();
    }
}
