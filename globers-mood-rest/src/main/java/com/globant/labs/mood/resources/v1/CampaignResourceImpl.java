package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.CampaignResource;
import com.globant.labs.mood.service.CampaignService;
import com.google.appengine.api.search.checkers.Preconditions;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

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
@Path("/api/v1/campaign")
public class CampaignResourceImpl extends AbstractResource implements CampaignResource {

    @Inject
    private CampaignService campaignService;

    @POST
    @Override
    public Response addCampaign(@RequestBody final Campaign campaign) {
        Preconditions.checkNotNull(campaign, "campaign cannot be null");
        return notNullResponse(campaignService.store(campaign));
    }

    @GET
    @Override
    public Response campaigns() {
        return notEmptyResponse(campaignService.campaigns());
    }

    @GET
    @Path("/{id}")
    @Override
    public Response campaign(@PathParam("id") final Long id) {
        return notNullResponse(campaignService.campaign(id));
    }

    @GET
    @Path("/{id}/start")
    @Override
    public Response startCampaign(@PathParam("id") final long id) {
        campaignService.start(id);
        return Response.ok().build();
    }
}
