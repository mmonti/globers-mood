package com.globant.labs.mood.repository.data;

import com.globant.labs.mood.model.persistent.Customer;
import com.globant.labs.mood.model.persistent.Project;
import org.springframework.data.jpa.repository.Query;

/**
 * @author mauro.monti (mauro.monti@globant.com)
 */
public interface ProjectRepository extends GenericRepository<Project, Long> {

    /**
     *
     * @param customer
     * @return
     */
    @Query("select project from Project project where project.customer = (?1)")
    Project projectByCustomer(final Customer customer);

    /**
     *
     * @param customer
     * @return
     */
    @Query("select project from Project project where project.name = (?1)")
    Project projectByName(final String name);

}