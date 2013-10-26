package com.globant.labs.mood.resources;

import com.sun.jersey.core.header.FormDataContentDisposition;

import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface TemplateResource {

    /**
     *
     * @return
     */
    Response templates();

    /**
     *
     * @param id
     * @return
     */
    Response template(final Long id);

    /**
     *
     * @param inputStream
     * @param formDataContentDisposition
     * @return
     */
    Response addTemplate(InputStream inputStream, FormDataContentDisposition formDataContentDisposition);
}