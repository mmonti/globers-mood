package com.globant.labs.mood.support.filter;

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Cross Origin Resource Sharing Filter.
 *
 * @author mauro.monti
 */
public class CORSFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(CORSFilter.class);

    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN_VALUE = "*";
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";

    public static final String JOINER_SEPARATOR = ", ";

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        logger.debug("initializing CORSFilter");
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
        ((HttpServletResponse) response).addHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        ((HttpServletResponse) response).addHeader(ACCESS_CONTROL_ALLOW_METHODS, Joiner.on(JOINER_SEPARATOR).join(HTTP_METHODS.values()));

        final String acrHeaders = ((HttpServletRequest) request).getHeader(ACCESS_CONTROL_REQUEST_HEADERS);
        if (acrHeaders != null && !acrHeaders.isEmpty()) {
            ((HttpServletResponse) response).addHeader(ACCESS_CONTROL_ALLOW_HEADERS, acrHeaders);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.debug("destroying CORSFilter");
    }

    /**
     *
     */
    enum HTTP_METHODS {
        GET, POST, PUT, DELETE, OPTIONS
    }
}
