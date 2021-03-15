package com.zup.proposal.financialproposal.client.account;

import com.zup.proposal.financialproposal.client.ProposalApiRequest;
import com.zup.proposal.financialproposal.client.account.response.CreditCardResponse;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class AccountClientFallback implements FallbackFactory<AccountClient> {
    @Override
    public AccountClient create(Throwable throwable) {
        return new AccountClient() {
            @Override
            public Optional<CreditCardResponse> requestCreditCard(ProposalApiRequest request) {
                return Optional.empty();
            }

            @Override
            public Optional<Map<String, String>> blockCreditCard(String number, Map<String, String> request) {
                return Optional.empty();
            }
        };
    }
}
