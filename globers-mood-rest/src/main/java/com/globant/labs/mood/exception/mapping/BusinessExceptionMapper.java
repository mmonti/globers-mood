package com.globant.labs.mood.exception.mapping;

import com.globant.labs.mood.exception.BaseResourceException;
import com.globant.labs.mood.exception.BusinessException;
import com.sun.jersey.api.view.Viewable;
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

    private static final String UNDERSCORE = "_";
    private static final String DASH = "-";
    private static final String FORWARD_SLASH = "/";

    private static Map<BusinessException.ErrorCode, Response.Status> mappings;

    static {
        mappings = new HashMap<BusinessException.ErrorCode, Response.Status>();
        mappings.put(RESOURCE_NOT_FOUND, NOT_FOUND);
        mappings.put(ILLEGAL_ARGUMENT, CONFLICT);
        mappings.put(ILLEGAL_STATE, PRECONDITION_FAILED);
        mappings.put(NOT_SUPPORTED, NOT_ACCEPTABLE);
        mappings.put(NOT_ALLOWED, FORBIDDEN);
        mappings.put(EXPECTATION_FAILED, PRECONDITION_FAILED);
        mappings.put(INTERNAL_ERROR, INTERNAL_SERVER_ERROR);
        mappings.put(FEEDBACK_ALREADY_SUBMITTED, PRECONDITION_FAILED);
        mappings.put(CAMPAIGN_ALREADY_CLOSED, PRECONDITION_FAILED);
    }

    /**
     * @param exception
     * @return
     */
    @Override
    public Response toResponse(final BusinessException exception) {
        logger.debug("business precondition not met while processing the request", exception);

        final BusinessException.ErrorCode errorCode = exception.getErrorCode();
        if (exception.isRedirectToView()) {
            return Response.ok(new Viewable(getRedirectToView(errorCode), exception)).build();
        }
        return BaseResourceException.createResponse(getStatusFromCode(errorCode), exception);
    }

    /**
     * @param errorCode
     * @return
     */
    private Response.Status getStatusFromCode(final BusinessException.ErrorCode errorCode) {
        Response.Status status = mappings.get(errorCode);
        return status != null ? status : Response.Status.INTERNAL_SERVER_ERROR;
    }

    /**
     * @param errorCode
     * @return
     */
    private String getRedirectToView(BusinessException.ErrorCode errorCode) {
        return FORWARD_SLASH + errorCode.name().replaceAll(UNDERSCORE, DASH).toLowerCase();
    }
}