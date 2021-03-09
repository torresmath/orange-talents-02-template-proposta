package com.zup.proposal.financialproposal.client.analysis;

import com.zup.proposal.financialproposal.client.ProposalApiRequest;
import com.zup.proposal.financialproposal.client.analysis.response.AnalysisResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AnalysisClientFallback implements AnalysisClient {

    @Override
    public AnalysisResponse analysisProposalRequest(ProposalApiRequest request) {

        System.out.println("ERROR HANDLING");

        throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Conexao com Analise de Proposta falhou!");
//        return new AnalysisResponse();
    }
}
