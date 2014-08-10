package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.StatsResource;
import com.globant.labs.mood.service.StatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
@Path("/api/v1/stats")
public class StatsResourceImpl extends AbstractResource implements StatsResource {

    private static final Logger logger = LoggerFactory.getLogger(StatsResourceImpl.class);

    @Inject
    private StatsService statsService;

    @GET
    @Path("/global")
    @Override
    public Response statistics() {
        return notNullResponse(statsService.getGlobalStatistics());
    }

    @GET
    @Path("/metadata")
    @Override
    public Response metadata() {
        return notEmptyResponse(statsService.getMetaData());
    }

    @GET
    @Path("/feedback/weekly")
    @Override
    public Response weeklyFeedback() {
        return notEmptyResponse(statsService.weeklyFeedback());
    }
}
