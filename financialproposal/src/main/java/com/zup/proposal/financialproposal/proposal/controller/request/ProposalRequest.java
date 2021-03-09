package com.zup.proposal.financialproposal.proposal.controller.request;

import com.zup.proposal.financialproposal.client.request.AnalysisRequest;
import com.zup.proposal.financialproposal.common.annotations.CPForCNPJ;
import com.zup.proposal.financialproposal.proposal.model.Proposal;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class ProposalRequest {

    @NotBlank
    private final String name;

    @NotBlank
    @Email
    private final String email;

    @CPForCNPJ
    @NotBlank
    private final String document;

    @Positive
    @NotNull
    private final BigDecimal salary;

    @NotNull
    @Valid
    private final AddressRequest address;

    public ProposalRequest(@NotBlank String name, @NotBlank @Email String email, String document, @Positive BigDecimal salary, @NotNull AddressRequest address) {
        this.name = name;
        this.email = email;
        this.document = document;
        this.salary = salary;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDocument() { return document.replaceAll("[.-]", ""); }

    public BigDecimal getSalary() {
        return salary;
    }

    public AddressRequest getAddress() {
        return address;
    }

    public Proposal toModel() {
        return new Proposal(name, email, document, salary, address.toModel());
    }
}
