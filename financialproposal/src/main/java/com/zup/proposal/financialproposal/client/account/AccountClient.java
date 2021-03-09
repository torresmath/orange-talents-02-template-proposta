package com.zup.proposal.financialproposal.client.account;

import com.zup.proposal.financialproposal.client.ProposalApiRequest;
import com.zup.proposal.financialproposal.client.account.response.CreditCardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "account",
        url = "${api.account.url}"
)
public interface AccountClient {

    @PostMapping("/api/cartoes")
    CreditCardResponse requestCreditCard(ProposalApiRequest request);
}
