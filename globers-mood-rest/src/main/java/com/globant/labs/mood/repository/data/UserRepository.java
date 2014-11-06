package com.globant.labs.mood.repository.data;

import com.globant.labs.mood.model.persistent.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author mauro.monti (mauro.monti@globant.com)
 */
public interface UserRepository extends GenericRepository<User, Long> {

    /**
     * @param email
     * @return
     */
    @Query("select user from User user where user.email = ?1")
    User findByEmail(final String email);

    /**
     *
     * @param projectId
     * @return
     */
    @Query("select user from User user where user.project.id = ?1")
    List<User> findByProject(final Long projectId);
}