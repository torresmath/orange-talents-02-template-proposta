package com.zup.proposal.financialproposal.proposal.controller;

import com.zup.proposal.financialproposal.client.analysis.AnalysisClient;
import com.zup.proposal.financialproposal.proposal.controller.request.ProposalRequest;
import com.zup.proposal.financialproposal.proposal.controller.response.ProposalResponse;
import com.zup.proposal.financialproposal.proposal.model.Proposal;
import com.zup.proposal.financialproposal.proposal.repository.ProposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/proposal")
public class ProposalController {

    @Autowired
    private ProposalRepository repository;

    @Autowired
    private AnalysisClient client;

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody @Valid ProposalRequest request) {

        boolean duplicatedProposal = repository.existsByDocument(request.getDocument());

        if (duplicatedProposal) {
            return ResponseEntity.unprocessableEntity().build();
        }

        Proposal proposal = repository.save(request.toModel());
        proposal.submitToAnalysis(client);
        
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}")
                .buildAndExpand(proposal.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProposalResponse> get(@PathVariable Long id) {

        Optional<Proposal> possibleProposal = repository.findById(id);

        if (possibleProposal.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new ProposalResponse(possibleProposal.get()));
    }
}
