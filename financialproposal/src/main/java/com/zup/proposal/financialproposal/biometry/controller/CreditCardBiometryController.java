package com.zup.proposal.financialproposal.biometry.controller;

import com.zup.proposal.financialproposal.biometry.controller.request.BiometryRequest;
import com.zup.proposal.financialproposal.biometry.controller.response.BiometryResponse;
import com.zup.proposal.financialproposal.biometry.model.Biometry;
import com.zup.proposal.financialproposal.proposal.model.CreditCard;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/credit-card")
public class CreditCardBiometryController {

    @PersistenceContext
    private EntityManager manager;

    @PostMapping("/{id}/biometry")
    @Transactional
    public ResponseEntity<?> create(@PathVariable("id") Long idCreditCard, @RequestBody @Valid BiometryRequest request) {

        Optional<CreditCard> possibleCreditCard = Optional.ofNullable(manager.find(CreditCard.class, idCreditCard));

        if (possibleCreditCard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CreditCard creditCard = possibleCreditCard.get();

        Biometry biometry = manager.merge(request.toModel(creditCard));

        URI uri = ServletUriComponentsBuilder.fromCurrentServletMapping().pathSegment("api/credit-card/biometry/{idBiometry}")
                .buildAndExpand(biometry.getId()).toUri();
        
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/biometry/{idBiometry}")
    public ResponseEntity<?> getBiometry(@PathVariable Long idBiometry) {

        Optional<Biometry> possibleBiometry = Optional.ofNullable(manager.find(Biometry.class, idBiometry));

        if (possibleBiometry.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new BiometryResponse(possibleBiometry.get()));
    }
}
