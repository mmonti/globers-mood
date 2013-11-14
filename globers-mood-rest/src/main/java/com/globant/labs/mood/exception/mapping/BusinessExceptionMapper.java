package com.globant.labs.mood.exception.mapping;

import com.globant.labs.mood.exception.BaseResourceException;
import com.globant.labs.mood.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

import static com.globant.labs.mood.exception.BusinessException.ErrorCode.*;
import static javax.ws.rs.core.Response.Status.*;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Provider
@Component
@Singleton
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

    private static final Logger logger = LoggerFactory.getLogger(BusinessExceptionMapper.class);

    private Map<BusinessException.ErrorCode, Response.Status> mappings;

    public BusinessExceptionMapper() {
        mappings = new HashMap<BusinessException.ErrorCode, Response.Status>();
        mappings.put(RESOURCE_NOT_FOUND, NOT_FOUND);
        mappings.put(ILLEGAL_ARGUMENT, CONFLICT);
        mappings.put(ILLEGAL_STATE, PRECONDITION_FAILED);
        mappings.put(NOT_SUPPORTED, NOT_ACCEPTABLE);
        mappings.put(NOT_ALLOWED, FORBIDDEN);
        mappings.put(EXPECTATION_FAILED, INTERNAL_SERVER_ERROR);
        mappings.put(INTERNAL_ERROR, INTERNAL_SERVER_ERROR);
    }

    @Override
    public Response toResponse(final BusinessException exception) {
        logger.error("business precondition not met while processing the request", exception);
        return BaseResourceException.createResponse(getStatusFromCode(exception.getErrorCode()), exception);
    }

    private Response.Status getStatusFromCode(final BusinessException.ErrorCode errorCode) {
        Response.Status status = mappings.get(errorCode);
        return status != null ? status : Response.Status.INTERNAL_SERVER_ERROR;
    }
}
