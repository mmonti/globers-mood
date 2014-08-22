package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.StatisticsResource;
import com.globant.labs.mood.service.StatisticsService;
import com.google.appengine.api.search.checkers.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
@Path("/api/v1/stats")
public class StatisticsResourceImpl extends AbstractResource implements StatisticsResource {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsResourceImpl.class);

    @Inject
    private StatisticsService statisticsService;

    @GET
    @Path("/datastore/entity")
    @Override
    public Response datastoreEntities() {
        logger.info("method=datastoreEntities()");

        return notEmptyResponse(statisticsService.datastoreEntities());
    }

    @GET
    @Path("/feedback/weekly")
    @Override
    public Response weeklyFeedback() {
        logger.info("method=weeklyFeedback()");

        return notEmptyResponse(statisticsService.weeklyFeedback());
    }

    @GET
    @Path("/campaign/{campaignId}")
    @Override
    public Response campaignStatistics(@PathParam("campaignId") final Long campaignId) {
        Preconditions.checkNotNull(campaignId, "campaignId is null");

        logger.info("method=campaignStatistics(), args=[campaignId={}]", campaignId);
        return notNullResponse(statisticsService.campaignStatistics(campaignId));
    }
}
