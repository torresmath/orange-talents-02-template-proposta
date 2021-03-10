package com.zup.proposal.financialproposal.proposal.controller.response;

import com.zup.proposal.financialproposal.proposal.model.Proposal;
import com.zup.proposal.financialproposal.proposal.model.ProposalStatus;

import java.math.BigDecimal;

public class ProposalResponse {

    private final Long id;

    private final String name;

    private final String document;

    private final BigDecimal salary;

    private final ProposalStatus status;

    private final AddressResponse address;

    public ProposalResponse(Proposal proposal) {

        this.id = proposal.getId();
        this.name =proposal.getName();
        this.document = proposal.getDocument();
        this.salary = proposal.getSalary();
        this.status = proposal.getStatus();
        this.address = proposal.mapAddress(AddressResponse::new);
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public String getDocument() { return document; }

    public BigDecimal getSalary() { return salary; }

    public ProposalStatus getStatus() { return status; }

    public AddressResponse getAddress() { return address; }
}
