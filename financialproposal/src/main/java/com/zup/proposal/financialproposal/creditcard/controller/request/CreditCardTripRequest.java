package com.zup.proposal.financialproposal.creditcard.controller.request;

import com.zup.proposal.financialproposal.creditcard.model.CreditCard;
import com.zup.proposal.financialproposal.creditcard.model.CreditCardTrip;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class CreditCardTripRequest {

    @NotBlank
    private String destination;

    @Future
    @NotNull
    private LocalDate endDate;

    public String getDestination() { return destination; }

    public LocalDate getEndDate() { return endDate; }

    public CreditCardTrip toModel(String clientIp, String userAgent, CreditCard creditCard) {
        return new CreditCardTrip(destination, endDate, clientIp, userAgent, creditCard);
    }
}
