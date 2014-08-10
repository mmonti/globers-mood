package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.Frequency;
import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.CampaignResource;
import com.globant.labs.mood.service.CampaignService;
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public Response addCampaign(@RequestBody final Campaign campaign) {
        Preconditions.checkNotNull(campaign, "campaign cannot be null");
        return notNullResponse(campaignService.store(campaign));
    }

    @GET
    @Override
    public Response campaigns(
            @QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("size") @DefaultValue("10") Integer size,
            @QueryParam("property") @DefaultValue("created") String property,
            @QueryParam("direction") @DefaultValue("ASC") Sort.Direction direction) {
        return notNullResponse(campaignService.campaigns(new PageRequest(page, size, direction, property)));
    }

    @GET
    @Path("/{id}")
    @Override
    public Response campaign(@PathParam("id") final long id) {
        return notNullResponse(campaignService.campaign(id));
    }

    @POST
    @Path("/{id}/start")
    @Override
    public Response startCampaign(@PathParam("id") final long id) {
        campaignService.start(id);
        return Response.ok().build();
    }

    @POST
    @Path("/{id}/close")
    @Override
    public Response closeCampaign(@PathParam("id") final long id) {
        campaignService.close(id);
        return Response.ok().build();
    }
}
