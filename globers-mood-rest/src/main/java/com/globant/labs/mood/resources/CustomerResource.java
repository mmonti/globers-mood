package com.globant.labs.mood.resources;

import com.globant.labs.mood.model.persistent.Customer;

import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface CustomerResource {

    /**
     * @param customer
     * @return
     */
    Response addCustomer(final Customer customer);

    /**
     * @return
     */
    Response customers();

    /**
     * @param id
     * @return
     */
    Response customer(final Long customerId);
}
