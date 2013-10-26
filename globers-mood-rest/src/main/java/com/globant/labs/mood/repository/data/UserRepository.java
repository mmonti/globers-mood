package com.globant.labs.mood.repository.data;

import com.globant.labs.mood.model.persistent.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author mauro.monti (mauro.monti@globant.com)
 */
public interface UserRepository extends GenericRepository<User, Long> {

    /**
     *
     * @param email
     * @return
     */
    User findByEmail(final String email);

    /**
     *
     * @return
     */
    @Query("select u from User u where u.assigned = false ")
    List<User> findUnassignedUsers();

    /**
     *
     * @return
     */
    @Query("select u from User u where u.assigned = true")
    List<User> findAssignedUsers();
}