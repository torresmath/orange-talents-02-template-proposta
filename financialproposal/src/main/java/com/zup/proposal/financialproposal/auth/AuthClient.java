package com.zup.proposal.financialproposal.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(
        name = "auth",
        url = "${auth.uri}",
        fallbackFactory = AuthClientFallbackFactory.class
)
public interface AuthClient {

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Optional<TokenResponse> auth(@RequestBody MultiValueMap<String, String> request);
}
