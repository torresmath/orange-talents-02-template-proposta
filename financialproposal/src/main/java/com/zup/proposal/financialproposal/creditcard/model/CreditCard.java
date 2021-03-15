package com.zup.proposal.financialproposal.creditcard.model;

import com.zup.proposal.financialproposal.proposal.model.Proposal;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "credit_card")
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String number;

    @NotNull
    @NotBlank
    private String owner;

    @NotNull
    private LocalDateTime emissionDate;

    @NotNull
    @Positive
    private BigDecimal cardLimit;

    @OneToOne(cascade = CascadeType.MERGE)
    @NotNull
    private Proposal proposal;

//    @NotNull
    @OneToOne(cascade = CascadeType.MERGE)
    private CreditCardDue expiration;

    @OneToMany(mappedBy = "creditCard", cascade = CascadeType.MERGE)
    private Set<Biometry> biometries;

    @OneToMany(mappedBy = "creditCard", cascade = CascadeType.MERGE)
    private Set<CreditCardBlock> blocks;

    public boolean isCurrentlyBlocked() {
        return blocks.stream().anyMatch(b -> b.getStatus().equals(BlockStatus.ACTIVE));
    }

    public CreditCard(@NotNull @NotBlank String number, @NotNull @NotBlank String owner, @NotNull LocalDateTime emissionDate, @NotNull @Positive BigDecimal cardLimit, @NotNull CreditCardDue expiration, @NotNull Proposal proposal) {
        this.number = number;
        this.owner = owner;
        this.emissionDate = emissionDate;
        this.cardLimit = cardLimit;
        this.proposal = proposal;
        this.expiration = expiration;
    }

    /**
     * @deprecated hibernate
     */
    public CreditCard() { }

    public Long getId() {
        return id;
    }

    public void block(CreditCardBlock block) {
        Assert.state(!isCurrentlyBlocked(), "Impossível adicionar novo bloqueio. Cartão já está bloqueado");
        Assert.state(!isScheduledToBlock(), "Impossível adicionar novo bloqueio. Já existe um bloqueio agendado");

        blocks.add(block);
    }

    public boolean isScheduledToBlock() {
        return blocks.stream().anyMatch(b -> b.getStatus().equals(BlockStatus.SCHEDULED));
    }
}
