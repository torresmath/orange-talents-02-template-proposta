package com.zup.proposal.financialproposal.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }
}
