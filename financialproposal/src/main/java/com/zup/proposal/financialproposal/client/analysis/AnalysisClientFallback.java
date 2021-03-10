package com.zup.proposal.financialproposal.client.analysis;

import com.zup.proposal.financialproposal.client.analysis.response.AnalysisResponse;
import com.zup.proposal.financialproposal.client.analysis.response.AnalysisResult;
import feign.FeignException;
import feign.hystrix.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AnalysisClientFallback implements FallbackFactory<AnalysisClient> {

    @Override
    public AnalysisClient create(Throwable cause) {
        return request -> {
            int httpStatus = cause instanceof FeignException ? ((FeignException) cause).status() : 500;

            System.out.println("ERROR HANDLING: " + httpStatus);

            if (httpStatus == 422) {

                return new AnalysisResponse(AnalysisResult.COM_RESTICAO);
            }

            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Falha ao verificar eligibilidade da Proposta. Tente novamente");
        };
    }
}
