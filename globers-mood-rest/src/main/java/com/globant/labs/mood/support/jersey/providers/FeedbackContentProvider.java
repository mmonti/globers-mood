package com.globant.labs.mood.support.jersey.providers;

import com.globant.labs.mood.support.jersey.FeedbackContent;
import com.sun.jersey.core.impl.provider.entity.BaseFormProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
* Created by mmonti on 7/31/14.
*/
@Provider
@Produces({"application/x-www-form-urlencoded", "*/*"})
@Consumes({"application/x-www-form-urlencoded", "*/*"})
public final class FeedbackContentProvider extends BaseFormProvider<FeedbackContent> {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackContentProvider.class);

    /**
     *
     * @param type
     * @param genericType
     * @param annotations
     * @param mediaType
     * @return
     */
    public boolean isReadable(
            final Class<?> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType) {

        return type == FeedbackContent.class;
    }

    /**
     *
     * @param type
     * @param genericType
     * @param annotations
     * @param mediaType
     * @param httpHeaders
     * @param entityStream
     * @return
     * @throws IOException
     */
    public FeedbackContent readFrom(
            final Class<FeedbackContent> type,
            final Type genericType,
            final Annotation annotations[],
            final MediaType mediaType,
            final MultivaluedMap<String, String> httpHeaders,
            final InputStream entityStream) throws IOException {

        return readFrom(new FeedbackContent(), mediaType, entityStream);
    }

    /**
     *
     * @param type
     * @param genericType
     * @param annotations
     * @param mediaType
     * @return
     */
    public boolean isWriteable(
            final Class<?> type,
            final Type genericType,
            final Annotation[] annotations,
            final MediaType mediaType) {

        return type == FeedbackContent.class;
    }

    /**
     *
     * @param classType
     * @param type
     * @param genericType
     * @param annotations
     * @param mediaType
     * @param httpHeaders
     * @param entityStream
     * @throws IOException
     */
    public void writeTo(
            final FeedbackContent classType,
            final Class<?> type,
            final Type genericType,
            final Annotation annotations[],
            final MediaType mediaType,
            final MultivaluedMap<String, Object> httpHeaders,
            final OutputStream entityStream) throws IOException {

        writeTo(classType, mediaType, entityStream);
    }
}