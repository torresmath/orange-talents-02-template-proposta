package com.zup.proposal.financialproposal.proposal.repository;

import com.zup.proposal.financialproposal.proposal.model.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

    boolean existsByDocument(String document);
}
