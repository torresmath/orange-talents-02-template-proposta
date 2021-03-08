package com.zup.proposal.financialproposal.proposal.controller.request;

import com.zup.proposal.financialproposal.proposal.model.Address;

import javax.validation.constraints.NotBlank;

public class AddressRequest {

    @NotBlank
    private final String street;
    @NotBlank
    private final String number;
    @NotBlank
    private final String zipCode;

    public AddressRequest(@NotBlank String street, @NotBlank String number, @NotBlank String zipCode) {
        this.street = street;
        this.number = number;
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Address toModel() {
        return new Address(street, number, zipCode);
    }
}
