package com.globant.labs.mood.exception;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class    BusinessException extends RuntimeException {

    private ErrorCode code;

    public BusinessException(final String message, final ErrorCode code, final Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException(final String message, final ErrorCode code) {
        super(message);
        this.code = code;
    }

    public static enum ErrorCode {
        ILLEGAL_ARGUMENT,
        RESOURCE_NOT_FOUND,
        ILLEGAL_STATE,
        NOT_SUPPORTED,
        NOT_ALLOWED,
        EXPECTATION_FAILED,
        INTERNAL_ERROR;
    }

    public ErrorCode getErrorCode() {
        return code;
    }
}
