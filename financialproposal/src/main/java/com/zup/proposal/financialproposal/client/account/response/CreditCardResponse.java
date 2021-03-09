package com.zup.proposal.financialproposal.client.account.response;

import com.zup.proposal.financialproposal.proposal.model.CreditCard;
import com.zup.proposal.financialproposal.proposal.model.Proposal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreditCardResponse {

    private final String id;
    private final Long idProposta;
    private final LocalDateTime emitidoEm;
    private final String titular;
    private final BigDecimal limite;
    private final CreditCardDueResponse vencimento;

    public String getId() { return id; }

    public Long getIdProposta() { return idProposta; }

    public LocalDateTime getEmitidoEm() { return emitidoEm; }

    public String getTitular() { return titular; }

    public BigDecimal getLimite() { return limite; }

    public CreditCardDueResponse getVencimento() { return vencimento; }

    public CreditCardResponse(String id, Long idProposta, LocalDateTime emitidoEm, String titular, BigDecimal limite, CreditCardDueResponse vencimento) {
        this.id = id;
        this.idProposta = idProposta;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.limite = limite;
        this.vencimento = vencimento;
    }

    public CreditCard toCreditCard(Proposal proposal) {
        return new CreditCard(id, titular, emitidoEm, limite, vencimento.toCreditCardDue(), proposal);
    }
}
