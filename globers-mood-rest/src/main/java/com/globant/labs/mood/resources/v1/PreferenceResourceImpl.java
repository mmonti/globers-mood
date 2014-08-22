package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.model.persistent.PreferenceKey;
import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.PreferenceResource;
import com.globant.labs.mood.service.PreferenceService;
import com.google.appengine.api.search.checkers.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
@Path("/api/v1/preference")
public class PreferenceResourceImpl extends AbstractResource implements PreferenceResource {

    private static final Logger logger = LoggerFactory.getLogger(PreferenceResourceImpl.class);

    @Inject
    private PreferenceService preferenceService;

    @GET
    @Override
    public Response preferences() {
        return notEmptyResponse(preferenceService.preferences());
    }

    @GET
    @Path("/{preferenceKey}")
    @Override
    public Response preference(@PathParam("preferenceKey") final PreferenceKey preferenceKey) {
        Preconditions.checkNotNull(preferenceKey, "preferenceKey is null");

        logger.info("method=preference(), args=[preferenceKey={}]", preferenceKey);

        return notNullResponse(preferenceService.preference(preferenceKey));
    }

    @GET
    @Path("/namespace/{ns}")
    @Override
    public Response preferenceByNamespace(@PathParam("ns") final String namespace) {
        Preconditions.checkNotNull(namespace, "namespace is null");

        logger.info("method=preferenceByNamespace(), args=[namespace={}]", namespace);

        return notNullResponse(preferenceService.preferenceByNamespace(namespace));
    }

    @POST
    @Path("/{preferenceKey}/update/{value}")
    @Override
    public Response update(@PathParam("preferenceKey") final PreferenceKey preferenceKey, @PathParam("value") final String value) {
        Preconditions.checkNotNull(preferenceKey, "preferenceKey is null");
        Preconditions.checkNotNull(value, "value is null");

        logger.info("method=update(), args=[preferenceKey={}, value={}]", preferenceKey, value);

        return notNullResponse(preferenceService.update(preferenceKey, value));
    }

}
