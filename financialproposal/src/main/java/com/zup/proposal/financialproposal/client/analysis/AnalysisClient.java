package com.zup.proposal.financialproposal.client.analysis;

import com.zup.proposal.financialproposal.client.ProposalApiRequest;
import com.zup.proposal.financialproposal.client.analysis.response.AnalysisResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        value = "analysis",
        url = "${api.analysis.url}",
        fallbackFactory = AnalysisClientFallback.class
)
public interface AnalysisClient {

    @PostMapping(value = "/api/solicitacao")
    AnalysisResponse analysisProposalRequest(ProposalApiRequest request);
}
