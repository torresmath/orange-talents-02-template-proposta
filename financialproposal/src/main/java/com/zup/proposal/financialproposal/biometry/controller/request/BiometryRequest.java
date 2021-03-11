package com.zup.proposal.financialproposal.biometry.controller.request;

import com.zup.proposal.financialproposal.biometry.model.Biometry;
import com.zup.proposal.financialproposal.common.annotations.IsBase64;
import com.zup.proposal.financialproposal.proposal.model.CreditCard;

import javax.validation.constraints.NotBlank;

public class BiometryRequest {

    @NotBlank
    @IsBase64
    private String fingerprint;

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Biometry toModel(CreditCard creditCard) {
        return new Biometry(fingerprint, creditCard);
    }
}
