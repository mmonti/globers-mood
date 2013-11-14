package com.globant.labs.mood.repository.data;

import com.globant.labs.mood.model.persistent.Customer;
import org.springframework.data.jpa.repository.Query;

/**
 * @author mauro.monti (mauro.monti@globant.com)
 */
public interface CustomerRepository extends GenericRepository<Customer, Long> {

    /**
     *
     * @param name
     * @return
     */
    @Query("select customer from Customer customer where customer.name = ?1")
    Customer findByName(final String name);

}