package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.model.persistent.Customer;
import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.CustomerResource;
import com.globant.labs.mood.service.CustomerService;
import com.google.appengine.api.search.checkers.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
@Path("/api/v1/customer")
public class CustomerResourceImpl extends AbstractResource implements CustomerResource {

    private static final Logger logger = LoggerFactory.getLogger(CustomerResourceImpl.class);

    @Inject
    private CustomerService customerService;

    @GET
    @Override
    public Response customers() {
        logger.info("method=customers()");

        return notNullResponse(customerService.customers(new PageRequest(0, 100)));
    }

    @POST
    @Override
    public Response addCustomer(@RequestBody final Customer customer) {
        Preconditions.checkNotNull(customer, "customer is null");

        logger.info("method=addCustomer(), args=[customer={}]", customer);

        return notNullResponse(customerService.store(customer));
    }

    @GET
    @Path("/{customerId}")
    @Override
    public Response customer(@PathParam("customerId") final Long customerId) {
        Preconditions.checkNotNull(customerId, "customerId is null");

        logger.info("method=customer(), args=[customerId={}]", customerId);

        return notNullResponse(customerService.customer(customerId));
    }
}
