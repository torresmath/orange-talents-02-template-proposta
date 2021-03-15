package com.zup.proposal.financialproposal.creditcard.controller.response;

import com.zup.proposal.financialproposal.creditcard.model.Biometry;

public class BiometryResponse {

    private final Long idBiometry;
    private final Long idCreditCard;
    private final String fingerprint;

    public Long getIdBiometry() { return idBiometry; }

    public Long getIdCreditCard() { return idCreditCard; }

    public String getFingerprint() { return fingerprint; }

    public BiometryResponse(Biometry biometry) {

        this.idBiometry = biometry.getId();
        this.idCreditCard = biometry.getIdCreditCard();
        this.fingerprint = biometry.getFingerprint();
    }

}
