package com.zup.proposal.financialproposal.auth;

import com.zup.proposal.financialproposal.auth.AuthClient;
import com.zup.proposal.financialproposal.auth.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthClient authClient;

    @Value("${auth.scope}")
    private String scope;

    @Value("${auth.grant_type}")
    private String grantType;

    @Value("${auth.client_id}")
    private String clientId;

    @Value("${auth.client_secret}")
    private String clientSecret;

    @PostMapping
    public ResponseEntity<?> generate(@RequestBody @Valid AuthRequest request) {

        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        req.add("scope", scope);
        req.add("grant_type", grantType);
        req.add("client_id", clientId);
        req.add("client_secret", clientSecret);
        req.add("username", request.getUsername());
        req.add("password", request.getPassword());

        Optional<TokenResponse> auth = authClient.auth(req);

        if (auth.isEmpty()) {
            return ResponseEntity.badRequest().body("bad credentials");
        }

        return ResponseEntity.ok(auth.get());
    }
}
