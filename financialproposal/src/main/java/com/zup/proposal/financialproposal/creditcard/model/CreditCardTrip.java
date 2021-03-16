package com.zup.proposal.financialproposal.creditcard.model;

import com.zup.proposal.financialproposal.client.account.AccountClient;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
public class CreditCardTrip {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotBlank
    private String destination;

    @Future
    @NotNull
    @Column(columnDefinition = "DATE")
    private LocalDate endDate;

    private LocalDateTime notificationTimestamp;

    @NotBlank
    @NotNull
    private String clientIp;

    @NotBlank
    @NotNull
    private String agent;

    @ManyToOne
    @NotNull
    private CreditCard creditCard;

    // region deprecated
    /**
     * @deprecated hibernate
     */
    public CreditCardTrip() { }
    // endregion

    public CreditCardTrip(@NotNull @NotBlank String destination,
                          @Future @NotNull LocalDate endDate,
                          @NotBlank @NotNull String clientIp,
                          @NotBlank @NotNull String agent,
                          @NotNull CreditCard creditCard) {

        Assert.state(LocalDate.now().isBefore(endDate), "ERRO - Data de término da viagem precisa ser no futuro: " + endDate);
        Assert.notNull(creditCard, "ERRO - Impossível criar viagem sem um cartão");

        this.destination = destination;
        this.endDate = endDate;
        this.clientIp = clientIp;
        this.agent = agent;
        this.creditCard = creditCard;
    }

    public String getDestination() {
        return destination;
    }

    public void notifyTrip(AccountClient client) {

        Map<String, String> request = Map.of(
                "destino", destination,
                "validoAte", endDate.toString()
        );

        client.notifyCreditCardTrip(creditCard.getNumber(), request)
                .ifPresent(r -> this.notificationTimestamp = LocalDateTime.now());
    }

    public boolean scheduled() {
        return endDate.isAfter(LocalDate.now()) && notificationTimestamp == null;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
