package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.model.persistent.Template;
import com.globant.labs.mood.model.persistent.TemplateMetadata;
import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.TemplateResource;
import com.globant.labs.mood.service.TemplateService;
import com.google.appengine.api.search.checkers.Preconditions;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

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

    private static final Logger logger = LoggerFactory.getLogger(TemplateResourceImpl.class);

    @Inject
    private TemplateService templateService;

    @GET
    @Override
    public Response templates() {
        return notNullResponse(templateService.templates(new PageRequest(0, 100)));
    }

    @GET
    @Path("/{templateId}")
    @Override
    public Response template(@PathParam("templateId") final Long templateId) {
        Preconditions.checkNotNull(templateId, "templateId is null");

        logger.info("method=template(), args=[templateId={}]", templateId);

        return notNullResponse(templateService.template(templateId));
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Override
    public Response addTemplate(
            @FormDataParam("name") final String name,
            @FormDataParam("description") final String description,
            @FormDataParam("file") final InputStream inputStream,
            @FormDataParam("file") final FormDataContentDisposition formDataContentDisposition) {
        Preconditions.checkNotNull(name, "name is null");
        Preconditions.checkNotNull(description, "description is null");
        Preconditions.checkNotNull(inputStream, "inputStream is null");
        Preconditions.checkNotNull(formDataContentDisposition, "formDataContentDisposition is null");

        logger.info("method=addTemplate(), args=[name={}, description={} inputStream={}, formDataContentDisposition={}]", name, description, inputStream, formDataContentDisposition);

        return notNullResponse(templateService.store(name, description, formDataContentDisposition.getFileName(), inputStream));
    }

    @DELETE
    @Path("/{templateId}")
    @Override
    public Response deleteTemplate(@PathParam("templateId") final Long templateId) {
        Preconditions.checkNotNull(templateId, "templateId is null");

        logger.info("method=deleteTemplate(), args=[templateId={}]", templateId);

        templateService.remove(templateId);
        return Response.ok().build();
    }

    @GET
    @Path("/{templateId}/metadata")
    @Override
    public Response metadata(@PathParam("templateId") final Long templateId) {
        Preconditions.checkNotNull(templateId, "templateId is null");

        logger.info("method=metadata(), args=[templateId={}]", templateId);

        return notNullResponse(templateService.getMetadata(templateId));
    }

    @PUT
    @Path("/{templateId}/metadata")
    @Override
    public Response metadata(@PathParam("templateId") final Long templateId, @RequestBody TemplateMetadata metadata) {
        Preconditions.checkNotNull(templateId, "templateId is null");
        Preconditions.checkNotNull(metadata, "metadata is null");

        logger.info("method=metadata(), args=[templateId={}, metadata={}]", templateId, metadata);

        return notNullResponse(templateService.setMetadata(templateId, metadata));
    }
}
