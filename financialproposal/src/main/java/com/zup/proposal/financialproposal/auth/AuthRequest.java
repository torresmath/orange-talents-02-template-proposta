package com.zup.proposal.financialproposal.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotBlank;

public class AuthRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    @JsonProperty("grant_type")
    @Value("${auth.grant_type}")
    private String grantType;

    @JsonProperty("client_id")
    @Value("${auth.client_id}")
    private String clientId;

    @Value("${auth.client_secret}")
    private String clientSecret;

    @Value("${auth.scope}")
    private String scope;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getScope() {
        return scope;
    }
}
