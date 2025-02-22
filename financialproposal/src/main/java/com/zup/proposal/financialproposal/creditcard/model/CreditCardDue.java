package com.zup.proposal.financialproposal.creditcard.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
public class CreditCardDue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String identifier;

    @NotNull
    @Positive
    private int day;

    @NotNull
    private LocalDateTime createDate;

    public CreditCardDue(@NotNull @NotBlank String identifier, @NotNull @Positive int day, @NotNull LocalDateTime createDate) {
        this.identifier = identifier;
        this.day = day;
        this.createDate = createDate;
    }

    /**
     * @deprecated hibernate
     */
    public CreditCardDue() { }
}
