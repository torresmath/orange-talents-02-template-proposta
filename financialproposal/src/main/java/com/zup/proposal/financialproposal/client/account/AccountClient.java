package com.zup.proposal.financialproposal.client.account;

import com.zup.proposal.financialproposal.client.ProposalApiRequest;
import com.zup.proposal.financialproposal.client.account.response.CreditCardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;
import java.util.Optional;

@FeignClient(
        name = "account",
        url = "${api.account.url}",
        fallbackFactory = AccountClientFallback.class
)
public interface AccountClient {

    @PostMapping("/api/cartoes")
    Optional<CreditCardResponse> requestCreditCard(ProposalApiRequest request);

    @PostMapping("/api/cartoes/{number}/bloqueios")
    Optional<Map<String, String>> blockCreditCard(@PathVariable String number, Map<String, String> request);

    @PostMapping("/api/cartoes/{number}/avisos")
    Optional<Map<String, String>> notifyCreditCardTrip(@PathVariable String number, Map<String, String> request);

    @PostMapping("/api/cartoes/{number}/carteiras")
    Optional<Map<String, String>> submitWallet(@PathVariable String number, Map<String, String> request);
}
