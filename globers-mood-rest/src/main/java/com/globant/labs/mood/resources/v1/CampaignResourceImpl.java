package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.CampaignResource;
import com.globant.labs.mood.service.CampaignService;
import com.google.appengine.api.search.checkers.Preconditions;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

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
