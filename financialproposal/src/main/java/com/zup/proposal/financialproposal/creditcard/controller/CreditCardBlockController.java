package com.zup.proposal.financialproposal.creditcard.controller;

import com.zup.proposal.financialproposal.creditcard.model.CreditCard;
import com.zup.proposal.financialproposal.creditcard.model.CreditCardBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;

@RestController
@RequestMapping("/api/credit-card")
public class CreditCardBlockController {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private BlockRequestHeaders blockRequestHeaders;

    @PostMapping("/{id}/block")
    @Transactional
    public ResponseEntity<?> block(@PathVariable Long id) {
        String clientIp = blockRequestHeaders.getClientIp();
        String userAgent = blockRequestHeaders.getUserAgent();

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
}
