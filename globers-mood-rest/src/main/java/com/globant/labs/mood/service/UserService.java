package com.globant.labs.mood.service;

import com.globant.labs.mood.model.persistent.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

}
