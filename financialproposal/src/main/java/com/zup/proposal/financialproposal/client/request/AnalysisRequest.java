package com.zup.proposal.financialproposal.client.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AnalysisRequest {

    @NotNull
    private final Long idProposta;

    @NotNull
    @NotBlank
    private final String skaoskap; // SHOULD PRODUCE BAD REQUEST

    @NotNull
    @NotBlank
    private final String nome;

    public AnalysisRequest(Long idProposta, String skaoskap, String nome) {
        this.idProposta = idProposta;
        this.skaoskap = skaoskap;
        this.nome = nome;
    }

    public Long getIdProposta() { return idProposta; }

    public String getSkaoskap() { return skaoskap; }

    public String getNome() { return nome; }
}
