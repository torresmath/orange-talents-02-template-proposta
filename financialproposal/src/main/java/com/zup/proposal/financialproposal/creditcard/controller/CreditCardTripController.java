package com.zup.proposal.financialproposal.creditcard.controller;

import com.zup.proposal.financialproposal.client.account.AccountClient;
import com.zup.proposal.financialproposal.creditcard.controller.request.CreditCardTripRequest;
import com.zup.proposal.financialproposal.creditcard.model.CreditCard;
import com.zup.proposal.financialproposal.creditcard.model.CreditCardTrip;
import com.zup.proposal.financialproposal.creditcard.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.LockModeType;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/credit-card")
public class CreditCardTripController {

    @Autowired
    private RequestHeadersValidation headersValidator;

    @Autowired
    private AccountClient client;

    @Autowired
    private CreditCardRepository repository;

    @PostMapping("/{id}/trip")
    @Transactional
    public ResponseEntity<?> createTrip(@PathVariable Long id, @RequestBody @Valid CreditCardTripRequest request) {

        Optional<Map<String, String>> possibleHeaders = this.headersValidator.getHeaders();

        if (possibleHeaders.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<CreditCard> possibleCreditCard = repository.findById(id);

        if (possibleCreditCard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CreditCard creditCard = possibleCreditCard.get();

        if (creditCard.haveScheduledTrips()) {
            return ResponseEntity.unprocessableEntity().body("Já existem viagens futuras agendadas para este cartão");
        }

        Map<String, String> headers = possibleHeaders.get();
        CreditCardTrip trip = request.toModel(headers.get("ip_address"), headers.get("user_agent"), creditCard);

        creditCard.addTrip(trip);
        repository.save(creditCard);

        return ResponseEntity.ok().build();
    }

    @Transactional
    @Scheduled(fixedDelay = 10000)
    @Lock(LockModeType.PESSIMISTIC_READ)
    public void notifyTrips() {
        repository.findByTripsNotificationTimestampIsNullAndTripsEndDateGreaterThan(LocalDate.now(), PageRequest.of(0, 5))
                .forEach(cc -> cc.notifyTrip(client));
    }

}
