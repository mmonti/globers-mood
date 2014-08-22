package com.globant.labs.mood.resources;

import com.sun.jersey.core.header.FormDataContentDisposition;

import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface FileResource {

    /**
     * @param inputStream
     * @param formDataContentDisposition
     * @return
     */
    Response upload(final InputStream inputStream,
                    final FormDataContentDisposition formDataContentDisposition);


    /**
     * @param filename
     * @return
     */
    Response serve(final String filename);
}
