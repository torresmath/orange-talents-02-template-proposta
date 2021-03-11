package com.zup.proposal.financialproposal.biometry.model;

import com.zup.proposal.financialproposal.common.Base64Utils;
import com.zup.proposal.financialproposal.common.annotations.IsBase64;
import com.zup.proposal.financialproposal.proposal.model.CreditCard;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Biometry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @IsBase64
    @NotNull
    @NotBlank
    private String fingerprint;

    @NotNull
    @CreationTimestamp
    private LocalDateTime createDate = LocalDateTime.now();

    @ManyToOne
    @NotNull
    private CreditCard creditCard;

    public Biometry(@NotNull @NotBlank String fingerprint, @NotNull CreditCard creditCard) {

        Assert.state(Base64Utils.isBase64(fingerprint), "Impossivel cadastrar biometria pois fingerprint fornecido não é um Base64 válido");
        Assert.notNull(creditCard, "Impossível cadastrar biometria sem um cartão de crédito válido");

        this.fingerprint = fingerprint;
        this.creditCard = creditCard;
    }

    public Long getId() {
        return id;
    }

    /**
     * @deprecated hibernate
     */
    public Biometry() {
    }

    public Long getIdCreditCard() {
        return creditCard.getId();
    }

    public String getFingerprint() {
        return fingerprint;
    }
}
