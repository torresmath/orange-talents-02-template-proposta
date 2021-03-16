package com.zup.proposal.financialproposal.creditcard.model;

import com.zup.proposal.financialproposal.client.account.AccountClient;
import com.zup.proposal.financialproposal.proposal.model.Proposal;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private Set<Biometry> biometries = new HashSet<>();

    @OneToMany(mappedBy = "creditCard", cascade = CascadeType.MERGE)
    private Set<CreditCardBlock> blocks = new HashSet<>();

    @OneToMany(mappedBy = "creditCard", cascade = CascadeType.MERGE)
    private Set<CreditCardTrip> trips = new HashSet<>();

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
    public CreditCard() {
    }

    public Long getId() {
        return id;
    }

    public String getNumber() { return number; }

    public void scheduleBlock(CreditCardBlock block) {
        Assert.state(!isCurrentlyBlocked(), "Impossível adicionar novo bloqueio. Cartão já está bloqueado");
        Assert.state(!isScheduledToBlock(), "Impossível adicionar novo bloqueio. Já existe um bloqueio agendado");

        blocks.add(block);
    }

    public boolean isCurrentlyBlocked() {
        return this.blocks.stream().anyMatch(b -> b.getStatus().equals(BlockStatus.ACTIVE));
    }

    public boolean isScheduledToBlock() {
        return blocks.stream().anyMatch(b -> b.getStatus().equals(BlockStatus.SCHEDULED));
    }

    public void block(AccountClient client) {

        List<CreditCardBlock> scheduledBlocks = blocks.stream()
                .filter(b -> b.getStatus().equals(BlockStatus.SCHEDULED))
                .collect(Collectors.toList());

        Assert.state(scheduledBlocks.size() <= 1, "ERRO - Existem múltiplos bloqueios agendados para este cartão");
        Assert.state(scheduledBlocks.size() == 1, "ERRO - Não há bloqueios agendados para este cartão");

        scheduledBlocks.get(0).setAsActive(client, this.number);
    }

    public void addTrip(CreditCardTrip trip) {

        Assert.state(!haveScheduledTrips(), "ERRO - Já existem viagens agendadas para este cartão");

        trips.add(trip);
    }

    public void notifyTrip(AccountClient client) {
        scheduledTrips().forEach(t -> t.notifyTrip(client));
    }

    private Set<CreditCardTrip> scheduledTrips() {
        return trips.stream().filter(CreditCardTrip::scheduled)
                .collect(Collectors.toUnmodifiableSet());
    }

    public boolean haveScheduledTrips() {
        return trips.stream().anyMatch(t -> t.getEndDate().isAfter(LocalDate.now()));
    }
}
