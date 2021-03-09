package com.zup.proposal.financialproposal.proposal.repository;

import com.zup.proposal.financialproposal.proposal.model.Proposal;
import com.zup.proposal.financialproposal.proposal.model.ProposalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

    boolean existsByDocument(String document);
    Page<Proposal> findByStatusAndCreditCardIsNull(ProposalStatus status, Pageable pageable);
}
