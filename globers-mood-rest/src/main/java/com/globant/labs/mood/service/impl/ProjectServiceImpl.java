package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.exception.BusinessException;
import com.globant.labs.mood.model.persistent.Project;
import com.globant.labs.mood.model.persistent.User;
import com.globant.labs.mood.repository.data.ProjectRepository;
import com.globant.labs.mood.repository.data.UserRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.ProjectService;
import com.google.appengine.api.search.checkers.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Set;

import static com.globant.labs.mood.exception.BusinessException.ErrorCode.EXPECTATION_FAILED;
import static com.globant.labs.mood.support.StringSupport.on;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Service
public class ProjectServiceImpl extends AbstractService implements ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Inject
    private ProjectRepository projectRepository;
    @Inject
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<Project> projects(final Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Project store(final Project project) {
        Preconditions.checkNotNull(project, "project cannot be null");
        final Project storedProject = projectRepository.projectByName(project.getName());
        if (storedProject != null) {
            logger.debug("store - project with name=[{}] already existent", project.getName());
            throw new BusinessException(on("project with name=[{}] already existent.", project.getName()), EXPECTATION_FAILED);
        }
        return projectRepository.save(project);
    }

    @Transactional
    @Override
    public boolean assign(final long projectId, final long userId) {
        Preconditions.checkNotNull(projectId, "projectId cannot be null");
        Preconditions.checkNotNull(userId, "userId cannot be null");

        final Project project = projectRepository.findOne(projectId);
        if (project == null) {
            throw new BusinessException(on("project with id=[{}] not found", projectId), BusinessException.ErrorCode.RESOURCE_NOT_FOUND);
        }

        final User user = userRepository.findOne(userId);
        if (user == null) {
            throw new BusinessException(on("user with id=[{}] not found", userId), BusinessException.ErrorCode.RESOURCE_NOT_FOUND);
        }

        if (project.assign(user)) {
            projectRepository.saveAndFlush(project);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Transactional
    @Override
    public boolean release(final long projectId, final long userId) {
        Preconditions.checkNotNull(projectId, "projectId cannot be null");
        Preconditions.checkNotNull(userId, "userId cannot be null");

        final Project project = projectRepository.findOne(projectId);
        if (project == null) {
            throw new BusinessException(on("project with id=[{}] not found", projectId), BusinessException.ErrorCode.RESOURCE_NOT_FOUND);
        }

        final User user = userRepository.findOne(userId);
        if (user == null) {
            throw new BusinessException(on("user with id=[{}] not found", userId), BusinessException.ErrorCode.RESOURCE_NOT_FOUND);
        }

        if (project.release(user)) {
            projectRepository.save(project);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Transactional(readOnly = true)
    @Override
    public Project project(final long id) {
        Preconditions.checkNotNull(id, "id cannot be null");
        return projectRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Set<User> usersOfProject(final long id) {
        Preconditions.checkNotNull(id, "id cannot be null");
        final Project project = projectRepository.findOne(id);
        return project.getUsers();
    }
}
