package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.events.StatsEvent;
import com.globant.labs.mood.exception.EntityNotFoundException;
import com.globant.labs.mood.model.StatsEntry;
import com.globant.labs.mood.model.persistent.Project;
import com.globant.labs.mood.model.persistent.User;
import com.globant.labs.mood.repository.data.ProjectRepository;
import com.globant.labs.mood.repository.data.UserRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.ProjectService;
import com.google.appengine.api.search.checkers.Preconditions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Service
public class ProjectServiceImpl extends AbstractService implements ProjectService {

    @Inject
    private ProjectRepository projectRepository;
    @Inject
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Set<Project> projects() {
        return new HashSet<Project>(projectRepository.findAll());
    }

    @Transactional
    @Override
    public Project store(final Project project) {
        Preconditions.checkNotNull(project, "project cannot be null");
        publishAfterCommit(new StatsEvent(this, Project.class, StatsEntry.PROJECT_COUNT));
        return projectRepository.save(project);
    }

    @Transactional
    @Override
    public boolean assign(final Long projectId, final Long userId) {
        Preconditions.checkNotNull(projectId, "projectId cannot be null");
        Preconditions.checkNotNull(userId, "userId cannot be null");

        final User user = userRepository.findOne(userId);
        if (user == null) {
            throw new EntityNotFoundException(User.class, userId);
        }

        final Project project = projectRepository.findOne(projectId);
        if (project == null) {
            throw new EntityNotFoundException(Project.class, projectId);
        }

        project.assign(user);

        try {
            projectRepository.saveAndFlush(project);
            return Boolean.TRUE;

        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Transactional
    @Override
    public boolean release(final Long projectId, final Long userId) {
        Preconditions.checkNotNull(projectId, "projectId cannot be null");
        Preconditions.checkNotNull(userId, "userId cannot be null");

        final Project project = projectRepository.findOne(projectId);
        if (project == null) {
            throw new EntityNotFoundException(Project.class, projectId);
        }

        final User user = userRepository.findOne(userId);
        if (user == null) {
            throw new EntityNotFoundException(User.class, userId);
        }

        if (project.release(user)) {
            projectRepository.save(project);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Transactional(readOnly = true)
    @Override
    public Project project(final Long id) {
        Preconditions.checkNotNull(id, "id cannot be null");
        return projectRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Set<User> usersOfProject(final Long id) {
        Preconditions.checkNotNull(id, "id cannot be null");
        Project project = projectRepository.findOne(id);
        return project.getUsers();
    }
}