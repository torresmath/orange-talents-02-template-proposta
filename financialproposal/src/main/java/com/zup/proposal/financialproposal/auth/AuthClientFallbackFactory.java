package com.zup.proposal.financialproposal.auth;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthClientFallbackFactory implements FallbackFactory<AuthClient> {
    @Override
    public AuthClient create(Throwable cause) {
        return request -> Optional.empty();
    }
}
