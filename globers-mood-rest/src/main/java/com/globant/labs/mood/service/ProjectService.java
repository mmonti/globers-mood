package com.globant.labs.mood.service;

import com.globant.labs.mood.model.persistent.Project;
import com.globant.labs.mood.model.persistent.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface ProjectService {

    /**
     * @return
     */
    Page<Project> projects(final Pageable pageable);

    /**
     * @param project
     */
    Project store(final Project project);

    /**
     * @param projectId
     * @return
     */
    Project project(final Long projectId);

}
