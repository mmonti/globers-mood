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
    public Response submitFeedback(final FeedbackContent feedbackContainer) {
        Preconditions.checkNotNull(feedbackContainer.getCampaignId(), "campaignId cannot be null");
        Preconditions.checkNotNull(feedbackContainer.getEmail(), "email cannot be null");
        Preconditions.checkNotNull(feedbackContainer.getToken(), "token cannot be null");

        return Response.ok(new Viewable(FEEDBACK_STORED, feedbackService.store(feedbackContainer))).build();
    }

    @GET
    @Path("/user/{userId}")
    @Override
    public Response feedbackOfUser(@PathParam("userId") final long userId) {
        Preconditions.checkNotNull(userId, "userId cannot be null");
        return notEmptyResponse(feedbackService.feedbackOfUser(userId));
    }

    @GET
    @Path("/campaign/{campaignId}")
    @Override
    public Response feedbackOfCampaign(@PathParam("campaignId") final long campaignId) {
        Preconditions.checkNotNull(campaignId, "campaignId cannot be null");
        return notEmptyResponse(feedbackService.feedbackOfCampaign(campaignId));
    }

    @GET
    @Path("/campaign/{campaignId}/user/{userId}")
    @Override
    public Response feedbackOfUserCampaign(@PathParam("campaignId") final long campaignId, @PathParam("userId") final long userId) {
        Preconditions.checkNotNull(campaignId, "campaignId cannot be null");
        Preconditions.checkNotNull(userId, "userId cannot be null");
        return notEmptyResponse(feedbackService.feedbackOfUserCampaign(campaignId, userId));
    }
}
