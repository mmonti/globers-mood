package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.model.persistent.PreferenceKey;
import com.globant.labs.mood.model.persistent.Project;
import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.PreferenceResource;
import com.globant.labs.mood.resources.ProjectResource;
import com.globant.labs.mood.service.PreferenceService;
import com.globant.labs.mood.service.ProjectService;
import com.google.appengine.api.search.checkers.Preconditions;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

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

    @Inject
    private PreferenceService preferenceService;

    @GET
    @Path("/{preferenceKey}")
    @Override
    public Response preference(@PathParam("preferenceKey") final PreferenceKey preferenceKey) {
        return notNullResponse(preferenceService.preference(preferenceKey));
    }

    @POST
    @Path("/{preferenceKey}/update/{value}")
    @Override
    public Response update(@PathParam("preferenceKey") final PreferenceKey preferenceKey, @PathParam("value") final String value) {
        return notNullResponse(preferenceService.update(preferenceKey, value));
    }

    @GET
    @Override
    public Response preferences() {
        return notEmptyResponse(preferenceService.preferences());
    }
}