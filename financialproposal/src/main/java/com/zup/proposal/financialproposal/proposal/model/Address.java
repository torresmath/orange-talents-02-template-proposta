package com.zup.proposal.financialproposal.proposal.model;

import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Address {

    @NotNull
    @NotBlank
    private String street;

    @NotNull
    @NotBlank
    private String number;

    @NotNull
    @NotBlank
    private String zipCode;

    public Address(@NotNull @NotBlank String street, @NotNull @NotBlank String number, @NotNull @NotBlank String zipCode) {

        Assert.isTrue(!street.isBlank(), "Impossível salvar endereco com Rua inválida");
        Assert.isTrue(!number.isBlank(), "Impossível salvar endereco com Numero inválido");
        Assert.isTrue(!zipCode.isBlank(), "Impossível salvar endereco com CEP inválido");

        this.street = street;
        this.number = number;
        this.zipCode = zipCode;
    }

    /**
     * @deprecated hibernate use only
     */
    public Address() { }

    public String getNumber() { return number; }
}
