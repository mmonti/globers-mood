package com.globant.labs.mood.resources;

import com.globant.labs.mood.model.persistent.Template;
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
    Response template(final long id);

    /**
     *
     * @param template
     * @param inputStream
     * @param formDataContentDisposition
     * @return
     */
    Response addTemplate(final Template template, final InputStream inputStream, final FormDataContentDisposition formDataContentDisposition);
}