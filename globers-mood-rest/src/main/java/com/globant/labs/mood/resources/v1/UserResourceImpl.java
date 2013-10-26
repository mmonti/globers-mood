package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.model.persistent.User;
import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.UserResource;
import com.globant.labs.mood.service.UserService;
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
@Path("/api/v1/user")
public class UserResourceImpl extends AbstractResource implements UserResource {

    @Inject
    private UserService userService;

    @POST
    @Override
    public Response addUser(@RequestBody final User user) {
        Preconditions.checkNotNull(user, "user cannot be null");
        Preconditions.checkNotNull(user.getName(), "user.name cannot be null");
        Preconditions.checkNotNull(user.getEmail(), "user.email cannot be null");
        return notNullResponse(userService.store(user));
    }

    @GET
    @Override
    public Response users() {
        return notEmptyResponse(userService.users());
    }

    @GET
    @Path("/{id}")
    @Override
    public Response user(@PathParam("id") final Long id) {
        Preconditions.checkNotNull(id, "id cannot be null");
        return notNullResponse(userService.user(id));
    }

    @GET
    @Path("/assigned")
    @Override
    public Response assigned() {
        return notEmptyResponse(userService.assignedUsers());
    }

    @GET
    @Path("/unassigned")
    @Override
    public Response unassigned() {
        return notEmptyResponse(userService.unassignedUsers());
    }
}
