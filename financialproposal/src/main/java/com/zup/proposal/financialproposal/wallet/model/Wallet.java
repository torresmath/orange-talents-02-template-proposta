package com.zup.proposal.financialproposal.wallet.model;

import com.zup.proposal.financialproposal.client.account.AccountClient;
import com.zup.proposal.financialproposal.creditcard.model.CreditCard;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Entity
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Provider provider;

    @Enumerated(EnumType.STRING)
    @NotNull
    private WalletStatus status = WalletStatus.UNVERIFIED;

    @ManyToOne
    @NotNull
    private CreditCard creditCard;

    /**
     * @deprecated hibernate
     */
    public Wallet() { }

    public Wallet(@NotNull @NotBlank String email, @NotNull Provider provider, @NotNull CreditCard creditCard) {

        Assert.state(!creditCard.haveWalletWithProvider(provider),
                "ERRO - Impossível gerar carteira pois cartão já está vinculado a uma carteira do provedor: " + provider);
        Assert.state(email != null && !email.isBlank(),"ERRO - Impossível gerar carteira sem email");
        Assert.state(provider != null,"ERRO - Impossível gerar carteira sem provedor");

        this.email = email;
        this.provider = provider;
        this.creditCard = creditCard;
    }

    public Long getId() { return id; }

    public Provider getProvider() { return provider; }

    public WalletStatus getStatus() {
        return status;
    }

    public void submit(AccountClient client) {

        Map<String, String> request = Map.of(
                "email", this.email,
                "carteira", provider.name()
        );

        client.submitWallet(creditCard.getNumber(), request)
                .ifPresent(r -> this.status = WalletStatus.VERIFIED);
    }
}
