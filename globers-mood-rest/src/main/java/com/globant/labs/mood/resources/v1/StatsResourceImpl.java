package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.model.StatsEntry;
import com.globant.labs.mood.model.persistent.*;
import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.StatsResource;
import com.globant.labs.mood.service.*;
import com.google.appengine.api.memcache.AsyncMemcacheService;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.Stats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
@Path("/api/v1/stats")
public class StatsResourceImpl extends AbstractResource implements StatsResource {

    private static final Logger logger = LoggerFactory.getLogger(StatsResourceImpl.class);

    @Inject
    private StatsService statsService;

    @GET
    @Override
    public Response statistics() {
        return notNullResponse(statsService.getStats());
    }

    @POST
    @Override
    public void generate() {
        statsService.executeStatEntryHandler(StatsEntry.CAMPAIGN_COUNT, Campaign.class);
        statsService.executeStatEntryHandler(StatsEntry.USER_COUNT, User.class);
        statsService.executeStatEntryHandler(StatsEntry.TEMPLATE_COUNT, Template.class);
        statsService.executeStatEntryHandler(StatsEntry.CUSTOMER_COUNT, Customer.class);
        statsService.executeStatEntryHandler(StatsEntry.PROJECT_COUNT, Project.class);
        statsService.executeStatEntryHandler(StatsEntry.FEEDBACK_COUNT, Feedback.class);
    }

    @GET
    @Path("/{entity}/{entry}")
    @Override
    public Response entryStats(@PathParam("entity") final String entity, @PathParam("entry") final String entry) {
        return notNullResponse(null);
    }

}
