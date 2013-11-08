package com.globant.labs.mood.service;

import com.globant.labs.mood.model.persistent.Customer;

import java.util.Set;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface CustomerService {

    /**
     *
     * @return
     */
    Set<Customer> customers();

    /**
     *
     * @param customer
     * @return
     */
    Customer store(final Customer customer);

    /**
     *
     * @param id
     * @return
     */
    Customer customer(final long id);
}
