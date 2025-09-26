package com.f12.moitz.common.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EndPointLoggingFilter implements Filter {

    @Override
    public void doFilter(
            final ServletRequest servletRequest,
            final ServletResponse servletResponse,
            final FilterChain filterChain
    ) throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        final String clientIp = httpServletRequest.getRemoteAddr();
        final String userAgent = httpServletRequest.getHeader("User-Agent");
        final String uri = httpServletRequest.getRequestURI();
        final String method = httpServletRequest.getMethod();

        log.info("Request URI: {} | Method: {} | ClientIp: {} | User-Agent: {}", uri, method, clientIp, userAgent);
        filterChain.doFilter(httpServletRequest, servletResponse);
    }

}
