package com.zup.proposal.financialproposal.proposal;

import com.zup.proposal.financialproposal.client.request.AnalysisRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solicitacao")
public class TestController {

    @PostMapping
    public ResponseEntity post(@RequestBody AnalysisRequest request) {

        throw new IllegalArgumentException("ERROR!!");
    }
}
