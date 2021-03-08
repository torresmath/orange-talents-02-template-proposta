package com.zup.proposal.financialproposal.client;

import com.zup.proposal.financialproposal.client.request.AnalysisRequest;
import com.zup.proposal.financialproposal.client.response.AnalysisResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        value = "analysis",
        url = "http://localhost:9999",
        fallback = AnalysisClientFallbackFactory.class
)
public interface AnalysisClient {

    @PostMapping("/api/solicitacao")
    AnalysisResponse analysisProposalRequest(AnalysisRequest request);
}
