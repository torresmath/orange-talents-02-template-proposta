package com.zup.proposal.financialproposal.proposal.job;

import com.zup.proposal.financialproposal.proposal.model.Proposal;
import com.zup.proposal.financialproposal.proposal.model.ProposalStatus;
import com.zup.proposal.financialproposal.proposal.repository.ProposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetCreditCardForApprovedProposals {

    @Autowired
    private ProposalRepository repository;

    @Scheduled(fixedDelay = 5000)
    public void getCreditCard() {

        List<Proposal> proposals = repository.findByStatusAndCreditCardIsNull(ProposalStatus.ELEGIVEL);

        proposals.forEach(p -> System.out.println("p.getDocument() = " + p.getDocument()));
    }
}
