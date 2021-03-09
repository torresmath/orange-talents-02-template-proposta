package com.zup.proposal.financialproposal.proposal.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
}
