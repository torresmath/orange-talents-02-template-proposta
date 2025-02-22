package com.zup.proposal.financialproposal.proposal.model;

import com.zup.proposal.financialproposal.client.ProposalApiRequest;
import com.zup.proposal.financialproposal.client.account.AccountClient;
import com.zup.proposal.financialproposal.client.account.response.CreditCardResponse;
import com.zup.proposal.financialproposal.client.analysis.AnalysisClient;
import com.zup.proposal.financialproposal.client.analysis.response.AnalysisResponse;
import com.zup.proposal.financialproposal.creditcard.model.CreditCard;
import com.zup.proposal.financialproposal.proposal.repository.ProposalRepository;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

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
    private String email;

    @NotNull
    @NotBlank
    private String document;

    @Positive
    @NotNull
    @Column(columnDefinition = "Decimal(10,2)")
    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    private ProposalStatus status;

    @NotNull
    @Embedded
    private Address address;

    @OneToOne(mappedBy = "proposal", cascade = CascadeType.ALL)
    private CreditCard creditCard;

    public Proposal(@NotNull @NotBlank String name,
                    @NotNull @NotBlank String email,
                    @NotNull @NotBlank String document,
                    @Positive @NotNull BigDecimal salary,
                    @NotNull Address address) {

        Assert.isTrue(!name.isBlank(), "Impossivel criar Proposta com nome invalido");
        Assert.isTrue(!email.isBlank(), "Impossivel criar Proposta com email invalido");
        Assert.isTrue(!document.isBlank(), "Impossivel criar Proposta com documento invalido");
        Assert.isTrue(salary.compareTo(BigDecimal.ZERO) > 0, "Impossivel criar Proposta com salário invalido: " + salary);
        Assert.notNull(address, "Impossivel criar Proposta sem endereco");

        String rawDocument = document.replaceAll("[.-]", "");

        String hexSalt = Hex.toHexString(UUID.randomUUID().toString().getBytes());
        String hexDocument = Hex.toHexString(rawDocument.getBytes());

        String encryptDocument = Encryptors.text(hexDocument, hexSalt).encrypt(rawDocument);

        this.name = name;
        this.email = email;
        this.document = encryptDocument;
        this.salary = salary;
        this.address = address;
    }

    /**
     * @deprecated hibernate
     */
    public Proposal() {
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return address.getNumber();
    }

    public String getDocument() {
        return document;
    }

    public void submitToAnalysis(AnalysisClient client) {

        AnalysisResponse respose = client.analysisProposalRequest(new ProposalApiRequest(id, document, name));
        this.status = respose.getProposalStatus();

    }

    @Transactional
    public void generateCreditCard(AccountClient client, ProposalRepository repository) {

        if (status == ProposalStatus.NAO_ELEGIVEL || this.creditCard != null) {
            return;
        }

        Optional<CreditCardResponse> response = client.requestCreditCard(new ProposalApiRequest(id, document, name));
        response.ifPresent(r -> {
            this.creditCard = r.toCreditCard(this);
            repository.save(this);
        });
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public ProposalStatus getStatus() {
        return status;
    }

    public <T> T mapAddress(Function<Address, T> mapper) {
        return mapper.apply(address);
    }
}
