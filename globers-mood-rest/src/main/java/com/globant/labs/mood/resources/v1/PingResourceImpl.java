package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.PingResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
@Path("/api/v1")
public class PingResourceImpl extends AbstractResource implements PingResource {

    private static final Logger logger = LoggerFactory.getLogger(PingResourceImpl.class);

    @GET
    @Path("/ping")
    @Override
    public Response ping() {
        logger.info("method=ping()");
        return Response.ok(System.currentTimeMillis()).build();
    }
}
