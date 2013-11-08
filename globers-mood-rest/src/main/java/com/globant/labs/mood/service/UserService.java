package com.globant.labs.mood.service;

import com.globant.labs.mood.model.persistent.User;

import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface UserService {

    /**
     * @return
     */
    Set<User> users();

    /**
     * @param user
     * @return
     */
    User store(final User user);

    /**
     * @param id
     * @return
     */
    User user(final long id);

    /**
     *
     * @param email
     * @return
     */
    User userByEmail(final String email);

    /**
     *
     * @return
     */
    Set<User> assignedUsers();

    /**
     *
     * @return
     */
    Set<User> unassignedUsers();

}
