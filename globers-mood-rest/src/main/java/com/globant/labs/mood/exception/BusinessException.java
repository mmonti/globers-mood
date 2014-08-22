package com.globant.labs.mood.exception;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class BusinessException extends RuntimeException {

    private ErrorCode code;
    private boolean redirectToView = false;

    /**
     * @param message
     * @param code
     * @param cause
     * @param redirectToView
     */
    public BusinessException(final String message, final ErrorCode code, final Throwable cause, final boolean redirectToView) {
        this(message, code, cause);
        this.redirectToView = redirectToView;
    }

    /**
     * @param message
     * @param code
     * @param cause
     */
    public BusinessException(final String message, final ErrorCode code, final Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * @param message
     * @param code
     * @param redirectToView
     */
    public BusinessException(final String message, final ErrorCode code, final boolean redirectToView) {
        this(message, code);
        this.redirectToView = redirectToView;
    }

    /**
     * @param message
     * @param code
     */
    public BusinessException(final String message, final ErrorCode code) {
        super(message);
        this.code = code;
    }

    /**
     * @return
     */
    public boolean isRedirectToView() {
        return redirectToView;
    }

    /**
     *
     */
    public static enum ErrorCode {
        ILLEGAL_ARGUMENT,
        RESOURCE_NOT_FOUND,
        ILLEGAL_STATE,
        NOT_SUPPORTED,
        NOT_ALLOWED,
        EXPECTATION_FAILED,
        INTERNAL_ERROR,
        FEEDBACK_ALREADY_SUBMITTED,
        CAMPAIGN_ALREADY_CLOSED;
    }

    /**
     * @return
     */
    public ErrorCode getErrorCode() {
        return code;
    }
}
