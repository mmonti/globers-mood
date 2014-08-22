package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.Template;
import com.globant.labs.mood.model.persistent.TemplateMetadata;
import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.CampaignResource;
import com.globant.labs.mood.service.CampaignService;
import com.globant.labs.mood.service.TemplateService;
import com.google.appengine.api.search.checkers.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
@Path("/api/v1/campaign")
public class CampaignResourceImpl extends AbstractResource implements CampaignResource {

    private static final Logger logger = LoggerFactory.getLogger(CampaignResourceImpl.class);

    @Inject
    private CampaignService campaignService;

    @Inject
    private TemplateService templateService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public Response addCampaign(@RequestBody final Campaign campaign) {
        Preconditions.checkNotNull(campaign, "campaign is null");

        logger.info("method=addCampaign(), args=[campaign={}]", campaign);

        return notNullResponse(campaignService.store(campaign));
    }

    @GET
    @Override
    public Response campaigns(
            @QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("size") @DefaultValue("10") Integer size,
            @QueryParam("property") @DefaultValue("created") String property,
            @QueryParam("direction") @DefaultValue("ASC") Sort.Direction direction) {

        logger.info("method=campaigns(), args=[page={}, size={}, property={}, direction={}]", page, size, property, direction);

        return notNullResponse(campaignService.campaigns(new PageRequest(page, size, direction, property)));
    }

    @GET
    @Path("/{campaignId}")
    @Override
    public Response campaign(@PathParam("campaignId") final Long campaignId) {
        Preconditions.checkNotNull(campaignId, "campaignId is null");

        logger.info("method=campaign(), args=[campaignId={}]", campaignId);

        return notNullResponse(campaignService.campaign(campaignId));
    }

    @POST
    @Path("/{campaignId}/start")
    @Override
    public Response startCampaign(@PathParam("campaignId") final Long campaignId) {
        Preconditions.checkNotNull(campaignId, "campaignId is null");

        logger.info("method=startCampaign(), args=[campaignId={}]", campaignId);
        campaignService.start(campaignId);

        return Response.ok().build();
    }

    @POST
    @Path("/{campaignId}/users/{userId}/remind")
    @Override
    public Response remind(@PathParam("campaignId") final Long campaignId, @PathParam("userId") final Long userId) {
        Preconditions.checkNotNull(campaignId, "campaignId is null");
        Preconditions.checkNotNull(userId, "userId is null");

        logger.info("method=remind(), args=[campaignId={}, userId={}]", campaignId, userId);

        campaignService.remind(campaignId, userId);
        return Response.ok().build();
    }

    @POST
    @Path("/{campaignId}/close")
    @Override
    public Response closeCampaign(@PathParam("campaignId") final Long campaignId) {
        Preconditions.checkNotNull(campaignId, "campaignId is null");

        logger.info("method=closeCampaign(), args=[campaignId={}]", campaignId);

        campaignService.close(campaignId);
        return Response.ok().build();
    }
}
