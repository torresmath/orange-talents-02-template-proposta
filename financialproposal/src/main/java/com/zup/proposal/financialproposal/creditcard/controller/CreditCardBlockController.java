package com.zup.proposal.financialproposal.creditcard.controller;

import com.zup.proposal.financialproposal.client.account.AccountClient;
import com.zup.proposal.financialproposal.creditcard.model.BlockStatus;
import com.zup.proposal.financialproposal.creditcard.model.CreditCard;
import com.zup.proposal.financialproposal.creditcard.model.CreditCardBlock;
import com.zup.proposal.financialproposal.creditcard.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;

@RestController
@RequestMapping("/api/credit-card")
public class CreditCardBlockController {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private RequestHeadersValidation requestHeadersValidation;

    @Autowired
    private CreditCardRepository repository;

    @Autowired
    private AccountClient client;

    @PostMapping("/{id}/block")
    @Transactional
    public ResponseEntity<?> block(@PathVariable Long id) {
        String clientIp = requestHeadersValidation.getClientIp();
        String userAgent = requestHeadersValidation.getUserAgent();

        Optional<ResponseEntity<?>> invalidHeaders = invalidHeaders(clientIp, userAgent);

        if (invalidHeaders.isPresent()) {
            return invalidHeaders.get();
        }

        CreditCard creditCard = manager.find(CreditCard.class, id);

        Optional<ResponseEntity<?>> invalidCreditCard = invalidCreditCard(creditCard);

        if (invalidCreditCard.isPresent()) {
            return invalidCreditCard.get();
        }

        creditCard.scheduleBlock(new CreditCardBlock(clientIp, userAgent, creditCard));

        manager.merge(creditCard);

        return ResponseEntity.ok().build();
    }

    Optional<ResponseEntity<?>> invalidHeaders(String clientIp, String userAgent) {

        if (clientIp == null) {
            return Optional.of(
                    ResponseEntity.badRequest().body("Impossível identificar endereço de IP do Host do Cliente")
            );
        }

        if (userAgent == null) {
            return Optional.of(
                    ResponseEntity.badRequest().body("Cabeçalho User-Agent é obrigatório")
            );
        }

        return Optional.empty();
    }

    Optional<ResponseEntity<?>> invalidCreditCard(CreditCard creditCard) {
        if (creditCard == null) {
            return Optional.of(
                    ResponseEntity.notFound().build()
            );
        }

        if (creditCard.isCurrentlyBlocked()) {
            return Optional.of(
                    ResponseEntity.unprocessableEntity().body("Cartão já está bloqueado")
            );
        }

        if (creditCard.isScheduledToBlock()) {
            return Optional.of(
                    ResponseEntity.unprocessableEntity().body("Já existe um bloqueio em andamento para este cartão")
            );
        }

        return Optional.empty();
    }

    @Async
    @Transactional
    @Scheduled(fixedDelay = 10000)
    @Lock(LockModeType.PESSIMISTIC_READ)
    public void blockScheduledCreditCards() {

        repository.findByBlocksStatus(BlockStatus.SCHEDULED, PageRequest.of(0, 5))
                .forEach(creditCard -> creditCard.block(client));
    }
}
