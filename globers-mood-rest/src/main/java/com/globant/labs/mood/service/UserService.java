package com.globant.labs.mood.service;

import com.globant.labs.mood.model.persistent.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface UserService {

    /**
     * @return
     */
    Page<User> users(final Pageable pageable);

    /**
     * @param user
     * @return
     */
    User store(final User user);

    /**
     * @param userId
     * @return
     */
    User user(final Long userId);

    /**
     * @param email
     * @return
     */
    User userByEmail(final String email);

    /**
     * @param projectId
     * @return
     */
    Set<User> usersOfProject(final Long projectId);

}
