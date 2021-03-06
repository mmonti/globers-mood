package com.globant.labs.mood.resources;

import com.globant.labs.mood.model.persistent.User;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface UserResource {

    /**
     * @param user
     * @return
     */
    Response addUser(final User user);

    /**
     * @return
     */
    Response users();

    /**
     * @param id
     * @return
     */
    Response user(final Long id);

    /**
     *
     * @param projectId
     * @return
     */
    Response usersOfProject(final Long projectId);
}
