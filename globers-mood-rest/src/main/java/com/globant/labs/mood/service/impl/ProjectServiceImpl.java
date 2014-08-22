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
import static com.globant.labs.mood.exception.BusinessException.ErrorCode.RESOURCE_NOT_FOUND;
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
        logger.info("method=projects(), args=[pageable=[{}]]", pageable);

        return projectRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Project store(final Project project) {
        Preconditions.checkNotNull(project, "project is null");

        logger.info("method=store(), args=[project=[{}]]", project);

        final Project storedProject = projectRepository.projectByName(project.getName());
        if (storedProject != null) {
            logger.error("method=store() - project name=[{}] already exist", project.getName());
            throw new BusinessException(on("Project with name=[{}] already exist.", project.getName()), EXPECTATION_FAILED);
        }
        return projectRepository.save(project);
    }

    @Transactional
    @Override
    public boolean assign(final Long projectId, final Long userId) {
        Preconditions.checkNotNull(projectId, "projectId is null");
        Preconditions.checkNotNull(userId, "userId is null");

        logger.info("method=assign(), args=[projectId=[{}], userId=[{}]]", projectId, userId);

        final Project project = project(projectId);
        if (project == null) {
            logger.error("method=assign() - projectId=[{}] not found", projectId);
            throw new BusinessException(on("Project with id=[{}] not found.", projectId), RESOURCE_NOT_FOUND);
        }

        final User user = userRepository.findOne(userId);
        if (user == null) {
            logger.error("method=assign() - userId=[{}] not found", userId);
            throw new BusinessException(on("User with id=[{}] not found.", userId), RESOURCE_NOT_FOUND);
        }

        logger.info("method=assign() - assigning userId=[{}] to projectId=[{}]", userId, projectId);
        if (project.assign(user)) {
            projectRepository.saveAndFlush(project);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Transactional
    @Override
    public boolean release(final Long projectId, final Long userId) {
        Preconditions.checkNotNull(projectId, "projectId is null");
        Preconditions.checkNotNull(userId, "userId is null");

        logger.info("method=release(), args=[projectId=[{}], userId=[{}]]", projectId, userId);

        final Project project = project(projectId);
        if (project == null) {
            logger.error("method=release() - projectId=[{}] not found", projectId);
            throw new BusinessException(on("Project with id=[{}] not found.", projectId), RESOURCE_NOT_FOUND);
        }

        final User user = userRepository.findOne(userId);
        if (user == null) {
            logger.error("method=release() - userId=[{}] not found", userId);
            throw new BusinessException(on("User with id=[{}] not found.", userId), RESOURCE_NOT_FOUND);
        }

        logger.info("method=release() - releasing userId=[{}] from projectId=[{}]", userId, projectId);
        if (project.release(user)) {
            projectRepository.save(project);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Transactional(readOnly = true)
    @Override
    public Project project(final Long projectId) {
        Preconditions.checkNotNull(projectId, "projectId is null");

        logger.info("method=project(), args=[projectId=[{}]]", projectId);

        return projectRepository.findOne(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public Set<User> usersOfProject(final Long projectId) {
        Preconditions.checkNotNull(projectId, "projectId is null");

        logger.info("method=usersOfProject(), args=[projectId=[{}]]", projectId);

        final Project project = projectRepository.findOne(projectId);
        return project.getUsers();
    }
}
