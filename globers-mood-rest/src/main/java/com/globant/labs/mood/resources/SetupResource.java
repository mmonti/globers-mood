package com.globant.labs.mood.resources;

import com.globant.labs.mood.model.setup.ImportInformation;
import com.sun.jersey.core.header.FormDataContentDisposition;

import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface SetupResource {

    /**
     * @param importInformation
     * @return
     */
    Response importData(final ImportInformation importInformation);

    /**
     *
     * @param inputStream
     * @return
     */
    Response importData(final InputStream inputStream, final FormDataContentDisposition formDataContentDisposition);
}
