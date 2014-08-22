package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.model.persistent.Project;
import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.ProjectResource;
import com.globant.labs.mood.service.ProjectService;
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
@Path("/api/v1/project")
public class ProjectResourceImpl extends AbstractResource implements ProjectResource {

    private static final Logger logger = LoggerFactory.getLogger(ProjectResourceImpl.class);

    @Inject
    private ProjectService projectService;

    @GET
    @Override
    public Response projects() {
        logger.info("method=projects()");

        return notNullResponse(projectService.projects(new PageRequest(0, 100)));
    }

    @POST
    @Override
    public Response addProject(@RequestBody final Project project) {
        Preconditions.checkNotNull(project, "project is null");

        logger.info("method=addProject(), args=[project={}]", project);

        return notNullResponse(projectService.store(project));
    }

    @POST
    @Path("/{projectId}/assign/{userId}")
    @Override
    public Response assignUser(@PathParam("projectId") final Long projectId, @PathParam("userId") final Long userId) {
        Preconditions.checkNotNull(projectId, "projectId is null");
        Preconditions.checkNotNull(userId, "userId is null");

        logger.info("method=assignUser(), args=[projectId={}, userId={}]", projectId, userId);

        return notNullResponse(projectService.assign(projectId, userId));
    }

    @POST
    @Path("/{projectId}/release/{userId}")
    @Override
    public Response releaseUser(@PathParam("projectId") final Long projectId, @PathParam("userId") final Long userId) {
        Preconditions.checkNotNull(projectId, "projectId cannot be null");
        Preconditions.checkNotNull(userId, "userId cannot be null");

        logger.info("method=releaseUser(), args=[projectId={}, userId={}]", projectId, userId);

        return notNullResponse(projectService.release(projectId, userId));
    }

    @GET
    @Path("/{projectId}/users")
    @Override
    public Response usersOfProject(@PathParam("projectId") Long projectId) {
        Preconditions.checkNotNull(projectId, "projectId cannot be null");

        logger.info("method=usersOfProject(), args=[projectId={}]", projectId);

        return notEmptyResponse(projectService.usersOfProject(projectId));
    }

}