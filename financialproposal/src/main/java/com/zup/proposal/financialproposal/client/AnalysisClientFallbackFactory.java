package com.zup.proposal.financialproposal.client;

import com.zup.proposal.financialproposal.client.request.AnalysisRequest;
import com.zup.proposal.financialproposal.client.response.AnalysisResponse;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

public class AnalysisClientFallbackFactory implements AnalysisClient {

    @Override
    public AnalysisResponse analysisProposalRequest(AnalysisRequest request) {

//        String errorMessage = cause.getLocalizedMessage();
//
//        System.out.println("errorMessage = " + errorMessage);

        throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Conexao com Analise de Proposta falhou!");
    }
}
