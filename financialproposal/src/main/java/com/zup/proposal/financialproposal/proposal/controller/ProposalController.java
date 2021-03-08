package com.zup.proposal.financialproposal.proposal.controller;

import com.zup.proposal.financialproposal.proposal.controller.request.ProposalRequest;
import com.zup.proposal.financialproposal.proposal.model.Proposal;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/proposal")
public class ProposalController {

    @PersistenceContext
    private EntityManager manager;

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody @Valid ProposalRequest request,
                                    UriComponentsBuilder uriBuilder) {

        Proposal proposal = request.toModel();

        manager.persist(proposal);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}")
                .buildAndExpand(proposal.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }
}
