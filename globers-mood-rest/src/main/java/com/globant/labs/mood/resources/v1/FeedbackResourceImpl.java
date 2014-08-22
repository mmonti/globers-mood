package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.FeedbackResource;
import com.globant.labs.mood.service.FeedbackService;
import com.globant.labs.mood.support.jersey.FeedbackContent;
import com.google.appengine.api.search.checkers.Preconditions;
import com.sun.jersey.api.view.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
@Path("/api/v1/feedback")
public class FeedbackResourceImpl extends AbstractResource implements FeedbackResource {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackResourceImpl.class);

    private static final String FEEDBACK_STORED = "/feedback-stored";

    @Inject
    private FeedbackService feedbackService;

    @POST
    @Path("/submit")
    @Produces(MediaType.TEXT_HTML)
    @Override
    public Response submitFeedback(final FeedbackContent feedbackContent) {
        Preconditions.checkNotNull(feedbackContent, "feedbackContent is null");

        logger.info("method=submitFeedback(), args=[feedbackContent={}]", feedbackContent);

        return Response.ok(new Viewable(FEEDBACK_STORED, feedbackService.store(feedbackContent))).build();
    }

    @GET
    @Path("/user/{userId}")
    @Override
    public Response feedbackOfUser(@PathParam("userId") final Long userId) {
        Preconditions.checkNotNull(userId, "userId is null");

        logger.info("method=feedbackOfUser(), args=[userId={}]", userId);

        return notEmptyResponse(feedbackService.feedbackOfUser(userId));
    }

    @GET
    @Path("/campaign/{campaignId}")
    @Override
    public Response feedbackOfCampaign(@PathParam("campaignId") final Long campaignId) {
        Preconditions.checkNotNull(campaignId, "campaignId is null");

        logger.info("method=feedbackOfCampaign(), args=[campaignId={}]", campaignId);

        return notEmptyResponse(feedbackService.feedbackOfCampaign(campaignId));
    }

    @GET
    @Path("/campaign/{campaignId}/user/{userId}")
    @Override
    public Response feedbackOfUser(@PathParam("campaignId") final Long campaignId, @PathParam("userId") final Long userId) {
        Preconditions.checkNotNull(campaignId, "campaignId is null");
        Preconditions.checkNotNull(userId, "userId is null");

        logger.info("method=feedbackOfUser(), args=[campaignId={}, userId={}]", campaignId, userId);

        return notNullResponse(feedbackService.feedbackOfUser(campaignId, userId));
    }
}
