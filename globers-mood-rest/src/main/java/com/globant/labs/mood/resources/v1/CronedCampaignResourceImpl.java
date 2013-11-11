package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.CronedCampaignResource;
import com.globant.labs.mood.service.CampaignService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
@Path("/api/v1/cron")
public class CronedCampaignResourceImpl extends AbstractResource implements CronedCampaignResource {

    @Inject
    private CampaignService campaignService;

    @POST
    @Path("/start")
    @Override
    public Response startScheduledCampaigns() {
        campaignService.startScheduledCampaigns();
        return Response.ok().build();
    }

    @POST
    @Path("/close")
    @Override
    public Response closeExpiredCampaigns() {
        campaignService.closeExpiredCampaigns();
        return Response.ok().build();
    }

}
