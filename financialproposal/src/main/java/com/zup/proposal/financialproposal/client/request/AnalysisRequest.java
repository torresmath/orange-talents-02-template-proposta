package com.zup.proposal.financialproposal.client.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AnalysisRequest {

    @NotNull
    private final Long idProposta;

    @NotNull
    @NotBlank
    private final String documento; // SHOULD PRODUCE BAD REQUEST

    @NotNull
    @NotBlank
    private final String nome;

    public AnalysisRequest(Long idProposta, String documento, String nome) {
        this.idProposta = idProposta;
        this.documento = documento;
        this.nome = nome;
    }

    public Long getIdProposta() { return idProposta; }

    public String getDocumento() { return documento; }

    public String getNome() { return nome; }
}
