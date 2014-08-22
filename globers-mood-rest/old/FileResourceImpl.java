package com.globant.labs.mood.resources.v1;

import com.globant.labs.mood.resources.AbstractResource;
import com.globant.labs.mood.resources.FileResource;
import com.globant.labs.mood.service.FileStorageService;
import com.google.common.base.Preconditions;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

import static com.globant.labs.mood.model.persistent.MimeType.getFromFile;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
@Path("/api/v1/files")
public class FileResourceImpl extends AbstractResource implements FileResource {

    private static final Logger logger = LoggerFactory.getLogger(FileResourceImpl.class);

    @Inject
    private FileStorageService fileStorageService;

    @Path("/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Override
    public Response upload(@FormDataParam("file") final InputStream inputStream,
                           @FormDataParam("file") final FormDataContentDisposition formDataContentDisposition) {

        return notNullResponse(fileStorageService.store(formDataContentDisposition.getFileName(), inputStream));
    }

    @GET
    @Path("/serve")
    public Response serve(@QueryParam("file") final String filename) {
        Preconditions.checkNotNull(filename, "filename is null");
        return notNullResponse(fileStorageService.getFile(filename), getFromFile(filename).getType());
    }

}
