package com.zup.proposal.financialproposal.client.analysis;

import com.zup.proposal.financialproposal.client.analysis.response.AnalysisResponse;
import com.zup.proposal.financialproposal.client.analysis.response.AnalysisResult;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class AnalysisClientFallback implements FallbackFactory<AnalysisClient> {

    @Override
    public AnalysisClient create(Throwable cause) {
        return request -> {

            System.out.println("ERROR HANDLING");

            return new AnalysisResponse(AnalysisResult.COM_RESTICAO);
        };
    }
}
