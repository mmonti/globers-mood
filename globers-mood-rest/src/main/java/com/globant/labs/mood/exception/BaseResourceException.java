package com.globant.labs.mood.exception;

import com.google.common.base.Preconditions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class BaseResourceException extends WebApplicationException {

    public static final String HEADER_ERROR_MESSAGE = "Error-Message";
    public static final String HEADER_ERROR_TRACE = "Error-Trace";

    /**
     *
     */
    public BaseResourceException() {
        super();
    }

    /**
     *
     * @param status
     */
    public BaseResourceException(final int status) {
        super(status);
    }

    /**
     *
     * @param response
     */
    public BaseResourceException(final Response response) {
        super(response);
    }

    /**
     *
     * @param status
     */
    public BaseResourceException(final Response.Status status) {
        super(status);
    }

    /**
     *
     * @param cause
     * @param status
     */
    public BaseResourceException(final Throwable cause, final int status) {
        super(cause, createResponse(Response.Status.fromStatusCode(status), cause));
    }

    /**
     *
     * @param cause
     * @param response
     */
    public BaseResourceException(final Throwable cause, final Response response) {
        super(cause, response);
    }

    /**
     *
     * @param cause
     * @param status
     */
    public BaseResourceException(final Throwable cause, final Response.Status status) {
        super(cause, status);
    }

    /**
     *
     * @param cause
     */
    public BaseResourceException(final Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param status
     * @param throwable
     */
    public BaseResourceException(final int status, final Throwable throwable) {
        this(Response.Status.fromStatusCode(status), throwable);
    }

    /**
     *
     * @param status
     * @param throwable
     */
    public BaseResourceException(final Response.Status status, final Throwable throwable) {
        super(createResponse(status, throwable));
    }

    /**
     * @param status
     * @param throwable
     * @return
     */
    public static Response createResponse(final Response.Status status, final Throwable throwable) {
        Preconditions.checkNotNull(status, "status is null");
        final Response.ResponseBuilder builder = Response.status(status);
        if (throwable != null) {
            addErrorHeaders(builder, throwable);
        }
        return builder.build();
    }

    /**
     * @param builder
     * @param throwable
     * @return
     */
    public static Response.ResponseBuilder addErrorHeaders(final Response.ResponseBuilder builder, final Throwable throwable) {
        Preconditions.checkNotNull(builder, "builder is null");
        Preconditions.checkNotNull(throwable, "builder is null");

        builder.header(HEADER_ERROR_MESSAGE, throwable.getMessage());
        builder.header(HEADER_ERROR_TRACE, throwable.getStackTrace()[0]);

        return builder;
    }
}
