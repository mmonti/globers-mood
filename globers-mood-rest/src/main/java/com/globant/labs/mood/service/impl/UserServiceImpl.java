package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.exception.BusinessException;
import com.globant.labs.mood.model.persistent.User;
import com.globant.labs.mood.repository.data.UserRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.UserService;
import com.google.appengine.api.search.checkers.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

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
        return userRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public User store(final User user) {
        Preconditions.checkNotNull(user, "user cannot be null");

        final User existentUser = userByEmail(user.getEmail());
        if (existentUser != null) {
            logger.debug("store - user with email=[{}] already existent", user.getEmail());
            throw new BusinessException(on("user with email=[{}] already existent.", user.getEmail()), EXPECTATION_FAILED);
        }
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User user(final long id) {
        Preconditions.checkNotNull(id, "id cannot be null");
        return userRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @Override
    public User userByEmail(final String email) {
        Preconditions.checkNotNull(email, "email cannot be null");
        final User user = this.userRepository.findByEmail(email);
        return user;
    }

}
