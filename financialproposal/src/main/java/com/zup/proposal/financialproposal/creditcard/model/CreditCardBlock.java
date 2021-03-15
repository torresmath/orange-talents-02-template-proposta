package com.zup.proposal.financialproposal.creditcard.model;

import com.zup.proposal.financialproposal.client.account.AccountClient;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

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

    private LocalDateTime blockDate;

    @ManyToOne
    private CreditCard creditCard;

    @Deprecated
    public CreditCardBlock() {
    }

    public BlockStatus getStatus() {
        return status;
    }

    public CreditCardBlock(@NotBlank @NotNull String clientIp, @NotBlank @NotNull String agent, CreditCard creditCard) {

        Assert.state(clientIp != null && !clientIp.isBlank(), "ERRO - Impossível executar bloqueio sem um clientIp");
        Assert.state(agent != null && !agent.isBlank(), "ERRO - Impossível executar bloqueio sem um User Agent");
        Assert.notNull(creditCard, "ERRO - Impossível executar bloqueio sem um cartão de crédito");

        this.clientIp = clientIp;
        this.agent = agent;
        this.creditCard = creditCard;
    }

    public void setAsActive(AccountClient client, @NotNull @NotBlank String number) {
        Assert.state(this.status.equals(BlockStatus.SCHEDULED),
                "ERRO - Impossível marcar bloqueio como ativo. Estado atual: " + this.status);

        client.blockCreditCard(number, Map.of("sistemaResponsavel", this.agent))
                .ifPresent(b -> {
                    this.status = BlockStatus.ACTIVE;
                    this.blockDate = LocalDateTime.now();
                });
    }
}
