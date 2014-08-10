package com.globant.labs.mood.exception.mapping;

import com.globant.labs.mood.exception.BaseResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Provider
@Component
@Singleton
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    private Logger logger = LoggerFactory.getLogger(WebApplicationExceptionMapper.class);

    @Override
    public Response toResponse(final WebApplicationException exception) {
        if (exception == null) {
            return Response.serverError().header(BaseResourceException.HEADER_ERROR_MESSAGE, "unexpected null exception").build();
        }
        // == Disable the error level to avoid duplicating exception information on logs.
        if (logger.isErrorEnabled()) {
            logger.error("Jersey exception has been thrown", exception);
        }

        final Response.ResponseBuilder builder = Response.fromResponse(exception.getResponse());
        return BaseResourceException.addErrorHeaders(builder, exception).build();
    }
}
