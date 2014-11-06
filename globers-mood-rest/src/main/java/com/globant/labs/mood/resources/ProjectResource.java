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
     * @return
     */
    Response projects();

}
