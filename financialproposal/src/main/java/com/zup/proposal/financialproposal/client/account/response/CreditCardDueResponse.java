package com.zup.proposal.financialproposal.client.account.response;

import com.zup.proposal.financialproposal.proposal.model.CreditCardDue;

import java.time.LocalDateTime;

public class CreditCardDueResponse {

    private final String id;

    private final int dia;

    private final LocalDateTime dataDeCriacao;

    public String getId() { return id; }

    public int getDia() { return dia; }

    public LocalDateTime getDataDeCriacao() { return dataDeCriacao; }

    public CreditCardDueResponse(String id, int dia, LocalDateTime dataDeCriacao) {
        this.id = id;
        this.dia = dia;
        this.dataDeCriacao = dataDeCriacao;
    }

    public CreditCardDue toCreditCardDue() { return new CreditCardDue(id, dia, dataDeCriacao); }
}
