package com.zup.proposal.financialproposal.client.account.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class CreditCardResponse {

    private String id;
    private Long idProposta;
    private LocalDateTime emitidoEm;
    private String titular;
    private BigDecimal limite;
    private Map<String, String> vencimento;

    public String getId() { return id; }

    public Long getIdProposta() { return idProposta; }

    public LocalDateTime getEmitidoEm() { return emitidoEm; }

    public String getTitular() { return titular; }

    public BigDecimal getLimite() { return limite; }

    public Map<String, String> getVencimento() { return vencimento; }
}
