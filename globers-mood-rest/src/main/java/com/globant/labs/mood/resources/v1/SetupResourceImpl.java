package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.model.setup.ImportInformation;
import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.SetupResource;
import com.globant.labs.mood.service.ImporterService;
import com.google.common.base.Preconditions;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
@Path("/api/v1/setup")
public class SetupResourceImpl extends AbstractResource implements SetupResource {

    @Inject
    private ImporterService importerService;

    @POST
    @Override
    public Response importData(@RequestBody final ImportInformation importInformation) {
        return notNullResponse(importerService.importData(importInformation));
    }

    @POST
    @Path("/file-import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Override
    public Response importData(
            @FormDataParam("file") final InputStream inputStream,
            @FormDataParam("file") final FormDataContentDisposition formDataContentDisposition) {

        Preconditions.checkNotNull(inputStream, "inputStream is null");
        return notNullResponse(importerService.importData(inputStream));
    }
}
