package com.zup.proposal.financialproposal.proposal.job;

import com.zup.proposal.financialproposal.client.account.AccountClient;
import com.zup.proposal.financialproposal.proposal.model.Proposal;
import com.zup.proposal.financialproposal.proposal.model.ProposalStatus;
import com.zup.proposal.financialproposal.proposal.repository.ProposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;

@Component
public class GetCreditCardForApprovedProposals {

    @Autowired
    private ProposalRepository repository;

    @Autowired
    private AccountClient client;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    public void getCreditCard() {
        Page<Proposal> proposals = repository.findByStatusAndCreditCardIsNull(ProposalStatus.ELEGIVEL, PageRequest.of(0, 5));
        proposals.forEach(p -> p.generateCreditCard(client, repository));
    }
}
