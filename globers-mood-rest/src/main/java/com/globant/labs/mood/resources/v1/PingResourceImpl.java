package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.repository.data.ProjectRepository;
import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.PingResource;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
@Path("/api/v1")
public class PingResourceImpl extends AbstractResource implements PingResource {

    @Inject
    private ProjectRepository projectRepository;

    @GET
    @Path("/ping")
    @Override
    public Response ping() {
        return Response.ok(System.currentTimeMillis()).build();
    }
}
