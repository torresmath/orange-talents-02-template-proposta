package com.zup.proposal.financialproposal.creditcard.controller;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class BlockRequestHeaders {

    private final HttpServletRequest request;

    public BlockRequestHeaders(HttpServletRequest request) {
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
}
