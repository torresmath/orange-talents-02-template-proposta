package com.zup.proposal.financialproposal.proposal.model;

import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String document;

    @Positive
    @NotNull
    @Column(columnDefinition = "Decimal(10,2)")
    private BigDecimal salary;

    @NotNull
    @Embedded
    private Address address;

    public Proposal(@NotNull @NotBlank String name, @NotNull @NotBlank String document, @Positive @NotNull BigDecimal salary, @NotNull Address address) {

        Assert.isTrue(!name.isBlank(), "Impossivel criar Proposta com nome invalido");
        Assert.isTrue(!document.isBlank(), "Impossivel criar Proposta com documento invalido");
        Assert.isTrue(salary.compareTo(BigDecimal.ZERO) > 0, "Impossivel criar Proposta com salário invalido: " + salary);
        Assert.notNull(address, "Impossivel criar Proposta sem endereco");

        this.name = name;
        this.document = document;
        this.salary = salary;
        this.address = address;
    }

    /**
     * @deprecated hibernate and tests use only
     */
    public Proposal() { }

    public Long getId() { return this.id; }

    public String getName() { return name; }

    public String getNumber() { return address.getNumber(); }
}
