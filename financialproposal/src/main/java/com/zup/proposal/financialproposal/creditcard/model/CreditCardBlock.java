package com.zup.proposal.financialproposal.creditcard.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class CreditCardBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    private String clientIp;

    @NotBlank
    @NotNull
    private String agent;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BlockStatus status = BlockStatus.SCHEDULED;

    @CreationTimestamp
    private LocalDateTime createDate = LocalDateTime.now();

    @ManyToOne
    private CreditCard creditCard;

    @Deprecated
    public CreditCardBlock() { }

    public BlockStatus getStatus() {
        return status;
    }

    public CreditCardBlock(@NotBlank @NotNull String clientIp, @NotBlank @NotNull String agent, CreditCard creditCard) {
        this.clientIp = clientIp;
        this.agent = agent;
        this.creditCard = creditCard;
    }
}
