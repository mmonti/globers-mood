package com.globant.labs.mood.repository.data;

import com.globant.labs.mood.model.persistent.Customer;
import com.globant.labs.mood.model.persistent.Project;
import org.springframework.data.jpa.repository.Query;

/**
 * @author mauro.monti (mauro.monti@globant.com)
 */
public interface ProjectRepository extends GenericRepository<Project, Long> {

    @Query("select p from Project p where p.customer = (?1)")
    Project projectByCustomer(Customer customer);

}