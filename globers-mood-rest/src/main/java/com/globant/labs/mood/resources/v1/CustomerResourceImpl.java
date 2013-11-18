package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.model.persistent.Customer;
import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.CustomerResource;
import com.globant.labs.mood.service.CustomerService;
import com.google.appengine.api.search.checkers.Preconditions;
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

    @Inject
    private CustomerService customerService;

    @POST
    @Override
    public Response addCustomer(@RequestBody final Customer customer) {
        Preconditions.checkNotNull(customer, "customer cannot be null");
        return notNullResponse(customerService.store(customer));
    }

    @GET
    @Override
    public Response customers() {
        return notNullResponse(customerService.customers(new PageRequest(0, 100)));
    }

    @GET
    @Path("/{id}")
    @Override
    public Response customer(@PathParam("id") final long id) {
        Preconditions.checkNotNull(id, "id cannot be null");
        return notNullResponse(customerService.customer(id));
    }
}
