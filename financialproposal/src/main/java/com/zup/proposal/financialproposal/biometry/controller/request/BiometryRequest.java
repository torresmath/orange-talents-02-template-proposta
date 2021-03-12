package com.zup.proposal.financialproposal.biometry.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zup.proposal.financialproposal.biometry.model.Biometry;
import com.zup.proposal.financialproposal.common.annotations.IsBase64;
import com.zup.proposal.financialproposal.proposal.model.CreditCard;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class BiometryRequest {

    @NotBlank
    @IsBase64
    @NotNull
    @JsonProperty("fingerprint")
    private String fingerprint;

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public Biometry toModel(CreditCard creditCard) {
        return new Biometry(fingerprint, creditCard);
    }
}
