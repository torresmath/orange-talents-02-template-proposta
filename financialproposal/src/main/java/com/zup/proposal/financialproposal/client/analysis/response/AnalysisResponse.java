package com.zup.proposal.financialproposal.client.analysis.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zup.proposal.financialproposal.proposal.model.ProposalStatus;

public class AnalysisResponse {

    public AnalysisResponse() { }

    public AnalysisResponse(AnalysisResult result) {
        this.result = result;
    }

    @JsonProperty("resultadoSolicitacao")
    private AnalysisResult result;

    public AnalysisResult getResult() { return result; }

    @JsonIgnore
    public ProposalStatus getProposalStatus() { return result.getProposalStatus(); }
}
