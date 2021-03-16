package com.zup.proposal.financialproposal.creditcard.controller;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@Component
public class RequestHeadersValidation {

    private final HttpServletRequest request;

    public RequestHeadersValidation(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Resolves client IP address when application is behind a NGINX or other reverse proxy server
     */
    public String getClientIp() {

        String xRealIp = request.getHeader("X-Real-IP");
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        String remoteAddr = request.getRemoteAddr();

        if (xRealIp != null)
            return xRealIp;

        if (xForwardedFor != null)
            return xForwardedFor;
        else
            return remoteAddr;
    }

    public String getUserAgent() {
        return request.getHeader("User-Agent");
    }

    public Optional<Map<String, String>> getHeaders() {
        String clientIp = getClientIp();

        if (clientIp == null)
            return Optional.empty();

        String userAgent = getUserAgent();

        if (userAgent == null)
            return Optional.empty();

        return Optional.of(
                Map.of(
                        "ip_address", clientIp,
                        "user_agent", userAgent
                )
        );
    }
}
