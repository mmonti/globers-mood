package com.globant.labs.mood.service.impl;

import com.globant.labs.mood.exception.EntityAlreadyExistentException;
import com.globant.labs.mood.model.persistent.User;
import com.globant.labs.mood.repository.data.UserRepository;
import com.globant.labs.mood.service.AbstractService;
import com.globant.labs.mood.service.UserService;
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
public class UserServiceImpl extends AbstractService implements UserService {

    @Inject
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Set<User> users() {
        return new HashSet<User>(userRepository.findAll());
    }

    @Transactional
    @Override
    public User store(final User user) {
        Preconditions.checkNotNull(user, "user cannot be null");

        final User existentUser = userByEmail(user.getEmail());
        if (existentUser != null) {
            throw new EntityAlreadyExistentException(User.class, user.getEmail());
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

    @Transactional(readOnly = true)
    @Override
    public Set<User> unassignedUsers() {
        return new HashSet<User>(this.userRepository.findUnassignedUsers());
    }

    @Transactional(readOnly = true)
    @Override
    public Set<User> assignedUsers() {
        return new HashSet<User>(this.userRepository.findAssignedUsers());
    }
}
