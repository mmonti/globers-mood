package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.CronedCampaignResource;
import com.globant.labs.mood.service.CampaignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
@Path("/api/v1/cron")
public class CronedCampaignResourceImpl extends AbstractResource implements CronedCampaignResource {

    private static final Logger logger = LoggerFactory.getLogger(CronedCampaignResourceImpl.class);

    @Inject
    private CampaignService campaignService;

    @GET
    @Path("/start")
    @Override
    public Response scheduledReadyToStart() {
        logger.info("method=scheduledReadyToStart()");

        campaignService.scheduledReadyToStart();
        return Response.ok().build();
    }

    @GET
    @Path("/start/pending")
    @Override
    public Response scheduledPendingToStart() {
        logger.info("method=scheduledPendingToStart()");

        return notEmptyResponse(campaignService.scheduledPendingToStart());
    }

    @GET
    @Path("/close")
    @Override
    public Response scheduledReadyToClose() {
        logger.info("method=scheduledReadyToClose()");

        campaignService.scheduledReadyToClose();
        return Response.ok().build();
    }

    @GET
    @Path("/close/pending")
    @Override
    public Response scheduledNextToExpire() {
        logger.info("method=scheduledNextToExpire()");

        return notEmptyResponse(campaignService.scheduledNextToExpire());
    }
}
