package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.model.persistent.Mood;
import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.FeedbackResource;
import com.globant.labs.mood.service.FeedbackService;
import com.google.appengine.api.search.checkers.Preconditions;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
@Path("/api/v1/feedback")
public class FeedbackResourceImpl extends AbstractResource implements FeedbackResource {

    @Inject
    private FeedbackService feedbackService;

    @POST
    @Path("/submit")
    @Override
    public Response submitFeedback(
            @FormParam("campaignId") final long campaignId,
            @FormParam("projectId") final long projectId,
            @FormParam("email") final String email,
            @FormParam("gmv") final Mood globerMoodValue,
            @FormParam("cmv") final Mood clientMoodValue,
            @FormParam("comment") final String comment) {

        Preconditions.checkNotNull(campaignId, "campaignId cannot be null");
        Preconditions.checkNotNull(projectId, "projectId cannot be null");
        Preconditions.checkNotNull(email, "email cannot be null");
        return notNullResponse(feedbackService.store(campaignId, projectId, email, globerMoodValue, clientMoodValue, comment));
    }

    @GET
    @Path("/user/{userId}")
    @Override
    public Response feedbackOfUser(@PathParam("campaignId") final long campaignId, @PathParam("userId") final long userId) {
        Preconditions.checkNotNull(userId, "userId cannot be null");
        return notEmptyResponse(feedbackService.feedbackOfUser(campaignId, userId));
    }

    @GET
    @Path("/campaign/{campaignId}")
    @Override
    public Response feedbackOfCampaign(@PathParam("campaignId") final long campaignId) {
        Preconditions.checkNotNull(campaignId, "campaignId cannot be null");
        return notEmptyResponse(feedbackService.feedbackOfCampaign(campaignId));
    }
}
