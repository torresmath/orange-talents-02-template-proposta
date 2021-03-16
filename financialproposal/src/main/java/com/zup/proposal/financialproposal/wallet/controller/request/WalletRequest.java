package com.zup.proposal.financialproposal.wallet.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zup.proposal.financialproposal.common.annotations.EnumValue;
import com.zup.proposal.financialproposal.creditcard.model.CreditCard;
import com.zup.proposal.financialproposal.wallet.model.Provider;
import com.zup.proposal.financialproposal.wallet.model.Wallet;
import org.springframework.util.Assert;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class WalletRequest {

    @Email
    @NotBlank
    private String email;

    @NotNull
    @EnumValue(targetEnum = Provider.class)
    @JsonProperty("provider")
    private String provider;

    public String getEmail() { return email; }

    public Provider getProvider() { return Provider.valueOf(provider); }

    public Wallet toModel(CreditCard creditCard) {
        Assert.notNull(creditCard, "Impossível solicitar carteira digital sem cartão de crédito");
        return new Wallet(email, getProvider(), creditCard);
    }
}
