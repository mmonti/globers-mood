package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.exception.BusinessException;
import com.globant.labs.mood.model.persistent.User;
import com.globant.labs.mood.repository.data.UserRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.UserService;
import com.google.appengine.api.search.checkers.Preconditions;
import com.google.common.collect.Sets;
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
public class UserServiceImpl extends AbstractService implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Inject
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<User> users(final Pageable pageable) {
        logger.info("method=users(), args=[pageable={}]", pageable);

        return userRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public User store(final User user) {
        Preconditions.checkNotNull(user, "user is null");

        logger.info("method=store(), args=[user={}]", user);

        final User existentUser = userByEmail(user.getEmail());
        if (existentUser != null) {
            logger.error("method=store() - user email=[{}] already exist", user.getEmail());
            throw new BusinessException(on("User with email=[{}] already exist.", user.getEmail()), EXPECTATION_FAILED);
        }
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User user(final Long userId) {
        Preconditions.checkNotNull(userId, "userId is null");

        logger.info("method=user(), args=[userId={}]", userId);

        return userRepository.findOne(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public User userByEmail(final String email) {
        Preconditions.checkNotNull(email, "email is null");

        logger.info("method=userByEmail(), args=[email={}]", email);

        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    @Override
    public Set<User> usersOfProject(final Long projectId) {
        Preconditions.checkNotNull(projectId, "projectId is null");

        logger.info("method=usersOfProject(), args=[projectId={}]", projectId);

        return Sets.newHashSet(userRepository.findByProject(projectId));
    }
}
