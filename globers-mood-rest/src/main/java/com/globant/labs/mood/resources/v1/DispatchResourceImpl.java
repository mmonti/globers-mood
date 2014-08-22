package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.DispatchResource;
import com.globant.labs.mood.service.MailingService;
import com.google.appengine.api.search.checkers.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
@Path("/api/v1/dispatch")
public class DispatchResourceImpl extends AbstractResource implements DispatchResource {

    private static final Logger logger = LoggerFactory.getLogger(DispatchResourceImpl.class);

    @Inject
    private MailingService mailingService;

    @POST
    @Path("/campaign/{campaignId}")
    @Override
    public Response dispatchMailMessages(@PathParam("campaignId") final Long campaignId) {
        Preconditions.checkNotNull(campaignId, "campaignId is null");

        logger.info("method=dispatchMailMessages(), args=[campaignId={}]", campaignId);

        mailingService.dispatch(campaignId);
        return Response.ok().build();
    }

    @POST
    @Path("/campaign/{campaignId}/user/{userId}")
    @Override
    public Response dispatchMailMessages(
            @PathParam("campaignId") final Long campaignId,
            @PathParam("userId") final Long userId) {

        Preconditions.checkNotNull(campaignId, "campaignId is null");
        Preconditions.checkNotNull(userId, "userId is null");

        logger.info("method=dispatchMailMessages(), args=[campaignId={}, userId={}]", campaignId, userId);

        mailingService.dispatch(campaignId, userId);
        return Response.ok().build();
    }
}
