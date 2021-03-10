package com.zup.proposal.financialproposal.proposal.controller.response;

import com.zup.proposal.financialproposal.proposal.model.Address;

public class AddressResponse {

    private final String street;

    private final String number;

    private final String zipCode;

    public AddressResponse(Address address) {
        this.street = address.getStreet();
        this.number = address.getNumber();
        this.zipCode = address.getZipCode();
    }

    public String getStreet() { return street; }

    public String getNumber() { return number; }

    public String getZipCode() { return zipCode; }
}
