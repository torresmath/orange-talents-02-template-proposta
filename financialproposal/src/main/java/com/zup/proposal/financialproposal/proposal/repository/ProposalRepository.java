package com.zup.proposal.financialproposal.proposal.repository;

import com.zup.proposal.financialproposal.proposal.model.Proposal;
import com.zup.proposal.financialproposal.proposal.model.ProposalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

    boolean existsByDocument(String document);
    List<Proposal> findByStatusAndCreditCardIsNull(ProposalStatus status);
}
