package com.zup.proposal.financialproposal.proposal.controller;

import com.zup.proposal.financialproposal.client.AnalysisClient;
import com.zup.proposal.financialproposal.client.request.AnalysisRequest;
import com.zup.proposal.financialproposal.client.response.AnalysisResponse;
import com.zup.proposal.financialproposal.proposal.controller.request.ProposalRequest;
import com.zup.proposal.financialproposal.proposal.model.Proposal;
import com.zup.proposal.financialproposal.proposal.repository.ProposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/proposal")
public class ProposalController {

    @Autowired
    private ProposalRepository repository;

    @Autowired
    private AnalysisClient client;

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody @Valid ProposalRequest request,
                                    UriComponentsBuilder uriBuilder) {

        boolean duplicatedProposal = repository.existsByDocument(request.getDocument());

        if (duplicatedProposal) {
            return ResponseEntity.unprocessableEntity().build();
        }

        Proposal proposal = repository.save(request.toModel());

        AnalysisResponse s = client.analysisProposalRequest(new AnalysisRequest(proposal.getId(), proposal.getDocument(), proposal.getName()));

        System.out.println("s.getResult().getProposalStatus() = " + s.getResult().getProposalStatus());
        
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}")
                .buildAndExpand(proposal.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }
}
