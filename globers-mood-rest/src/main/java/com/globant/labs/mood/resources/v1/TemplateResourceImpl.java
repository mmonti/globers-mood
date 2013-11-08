package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.TemplateResource;
import com.globant.labs.mood.service.TemplateService;
import com.google.appengine.api.search.checkers.Preconditions;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
@Path("/api/v1/template")
public class TemplateResourceImpl extends AbstractResource implements TemplateResource {

    @Inject
    private TemplateService templateService;

    @GET
    @Override
    public Response templates() {
        return notEmptyResponse(templateService.templates());
    }

    @GET
    @Path("/{id}")
    @Override
    public Response template(@PathParam("id") final long id) {
        Preconditions.checkNotNull(id, "id cannot be null");
        return notNullResponse(templateService.template(id));
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Override
    public Response addTemplate(
            @FormDataParam("file") final InputStream inputStream,
            @FormDataParam("file") final FormDataContentDisposition formDataContentDisposition) {
        Preconditions.checkNotNull(inputStream, "inputStream cannot be null");
        return notNullResponse(templateService.store(formDataContentDisposition.getName(), inputStream));
    }

}
