package com.globant.labs.mood.exception.mapping;

import com.globant.labs.mood.exception.BaseResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Provider
@Component
@Singleton
public class IllegalArgumentExceptionMapping implements ExceptionMapper<IllegalArgumentException> {

    private static final Logger logger = LoggerFactory.getLogger(IllegalArgumentExceptionMapping.class);

    /**
     * @param exception
     * @return
     */
    @Override
    public Response toResponse(final IllegalArgumentException exception) {
        logger.debug("Illegal argument exception detected", exception);
        return BaseResourceException.createResponse(Response.Status.BAD_REQUEST, exception);
    }
}
