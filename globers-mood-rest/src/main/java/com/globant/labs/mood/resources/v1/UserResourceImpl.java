package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.model.persistent.User;
import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.UserResource;
import com.globant.labs.mood.service.UserService;
import com.google.appengine.api.search.checkers.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
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

    private static final Logger logger = LoggerFactory.getLogger(UserResourceImpl.class);

    @Inject
    private UserService userService;

    @POST
    @Override
    public Response addUser(@RequestBody final User user) {
        Preconditions.checkNotNull(user, "user is null");
        Preconditions.checkNotNull(user.getName(), "user.name is null");
        Preconditions.checkNotNull(user.getEmail(), "user.email is null");

        logger.info("method=addUser(), args=[user={}]", user);

        return notNullResponse(userService.store(user));
    }

    @GET
    @Override
    public Response users() {
        logger.info("method=users()");

        return notNullResponse(userService.users(new PageRequest(0, 100)));
    }

    @GET
    @Path("/{userId}")
    @Override
    public Response user(@PathParam("userId") final Long userId) {
        Preconditions.checkNotNull(userId, "userId is null");

        logger.info("method=user(), args=[userId={}]", userId);

        return notNullResponse(userService.user(userId));
    }
}
