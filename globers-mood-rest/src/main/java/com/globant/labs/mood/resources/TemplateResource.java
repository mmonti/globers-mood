package com.globant.labs.mood.resources;

import com.globant.labs.mood.model.persistent.Template;
import com.globant.labs.mood.model.persistent.TemplateMetadata;
import com.sun.jersey.core.header.FormDataContentDisposition;

import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public interface TemplateResource {

    /**
     * @return
     */
    Response templates();

    /**
     * @param templateId
     * @return
     */
    Response template(final Long templateId);

    /**
     *
     * @param name
     * @param description
     * @param inputStream
     * @param formDataContentDisposition
     * @return
     */
    Response addTemplate(final String name, final String description, final InputStream inputStream, final FormDataContentDisposition formDataContentDisposition);

    /**
     * @param templateId
     * @return
     */
    Response deleteTemplate(final Long templateId);

    /**
     * @param templateId
     * @return
     */
    Response metadata(final Long templateId);

    /**
     *
     * @param templateId
     * @param metadata
     * @return
     */
    Response metadata(final Long templateId, final TemplateMetadata metadata);
}