package com.bank.userManagement.filter;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import java.io.IOException;
import java.util.UUID;

@Component
public class RequestTrackingFilter implements Filter {
    private static final String REQUEST_ID = "requestId";

    /**
     * Add unique request id so it can be tracked as it progresses through the system
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            String requestId = UUID.randomUUID().toString();
            MDC.put(REQUEST_ID, requestId);
            chain.doFilter(request, response);
        } finally {
            MDC.remove(REQUEST_ID);
        }
    }
}

