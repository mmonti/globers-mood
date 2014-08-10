package com.globant.labs.mood.service;

import com.globant.labs.mood.model.persistent.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface CustomerService {

    /**
     * @return
     */
    Page<Customer> customers(final Pageable pageable);

    /**
     * @param customer
     * @return
     */
    Customer store(final Customer customer);

    /**
     * @param id
     * @return
     */
    Customer customer(final long id);
}
