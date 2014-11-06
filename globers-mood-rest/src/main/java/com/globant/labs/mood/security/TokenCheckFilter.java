package com.globant.labs.mood.security;

import com.globant.labs.mood.exception.BusinessException;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author mmonti
 */
public class TokenCheckFilter extends OncePerRequestFilter {

    private static final String TOKEN_HEADER_NAME = "X-Auth-Token";
    private static final String TOKEN_PARAMETER_NAME = "token";
    private static final String OPTIONS_METHOD = "OPTIONS";

    private static final List<String> PUBLIC_PATHS = Lists.newArrayList(
        "/api/v1/feedback/**",
        "/api/v1/ping",
        "/api/v1/cron/**"
    );

    private final AntPathMatcher matcher = new AntPathMatcher();

    private String tokenValue = null;

    /**
     *
     * @param tokenValue
     */
    public TokenCheckFilter(final String tokenValue) {
        this.tokenValue = tokenValue;
    }

    /**
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws ServletException, IOException {
        for (String currentPath : PUBLIC_PATHS) {
            boolean match = matcher.match(currentPath, request.getPathInfo());
            if (match) {
                chain.doFilter(request, response);
                return;
            }
        }

        if (request.getMethod().equals(OPTIONS_METHOD)) {
            chain.doFilter(request, response);
            return;
        }

        final String token = resolveToken(request);
        if (token == null || token != null && !token.equals(this.tokenValue)) {
            throw new BusinessException("Provided token invalid or null", BusinessException.ErrorCode.EXPECTATION_FAILED);
        }

        chain.doFilter(request, response);
    }

    /**
     * @param httpServletRequest
     * @return
     */
    private String resolveToken(final HttpServletRequest httpServletRequest) {
        final String headerToken = httpServletRequest.getHeader(TOKEN_HEADER_NAME);
        if (StringUtils.hasText(headerToken)) {
            return headerToken;
        }
        final String parameterToken = httpServletRequest.getParameter(TOKEN_PARAMETER_NAME);
        if (StringUtils.hasText(parameterToken)) {
            return parameterToken;
        }
        return null;
    }
}
