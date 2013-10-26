package com.globant.labs.mood.resources;

import com.globant.labs.mood.model.persistent.Project;

import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface ProjectResource {

    /**
     * @param project
     * @return
     */
    Response addProject(final Project project);

    /**
     *
     * @param projectId
     * @param userId
     * @return
     */
    Response assignUser(final Long projectId, final Long userId);

    /**
     *
     * @param projectId
     * @param userId
     * @return
     */
    Response releaseUser(final Long projectId, final Long userId);

    /**
     *
     * @return
     */
    Response projects();

    /**
     *
     * @return
     */
    Response usersOfProject(final long projectId);
}