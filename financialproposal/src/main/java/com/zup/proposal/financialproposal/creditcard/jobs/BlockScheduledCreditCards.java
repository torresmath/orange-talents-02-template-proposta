package com.zup.proposal.financialproposal.creditcard.jobs;

import com.zup.proposal.financialproposal.client.account.AccountClient;
import com.zup.proposal.financialproposal.creditcard.model.BlockStatus;
import com.zup.proposal.financialproposal.creditcard.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;

@Component
public class BlockScheduledCreditCards {

    @Autowired
    private CreditCardRepository repository;

    @Autowired
    private AccountClient client;

//    @Scheduled(fixedDelay = 10000)
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    public void blockScheduledCreditCards() {

        repository.findByBlocksStatus(BlockStatus.SCHEDULED, PageRequest.of(0, 5))
                .forEach(creditCard -> creditCard.block(client));
    }
}
