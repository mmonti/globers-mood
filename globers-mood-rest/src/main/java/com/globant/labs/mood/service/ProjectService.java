package com.globant.labs.mood.service;

import com.globant.labs.mood.model.persistent.Project;
import com.globant.labs.mood.model.persistent.User;

import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface ProjectService {

    /**
     *
     * @return
     */
    Set<Project> projects();

    /**
     *
     * @param project
     */
    Project store(final Project project);

    /**
     *
     * @param projectId
     * @param userId
     * @return
     */
    boolean assign(final long projectId, final long userId);

    /**
     *
     * @param projectId
     * @param userId
     * @return
     */
    boolean release(final long projectId, final long userId);

    /**
     *
     * @param id
     * @return
     */
    Project project(final long id);

    /**
     *
     * @param id
     * @return
     */
    Set<User> usersOfProject(final long id);

}
