package com.zup.proposal.financialproposal.wallet.controller;

import com.zup.proposal.financialproposal.client.account.AccountClient;
import com.zup.proposal.financialproposal.creditcard.model.CreditCard;
import com.zup.proposal.financialproposal.wallet.controller.request.WalletRequest;
import com.zup.proposal.financialproposal.wallet.model.Wallet;
import com.zup.proposal.financialproposal.wallet.model.WalletStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private AccountClient client;

    @PostMapping("/credit-card/{id}")
    @Transactional
    public ResponseEntity<?> create(@PathVariable Long id, @RequestBody @Valid WalletRequest request) {

        Optional<CreditCard> possibleCreditCard = Optional.ofNullable(manager.find(CreditCard.class, id));

        if (possibleCreditCard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CreditCard creditCard = possibleCreditCard.get();

        if (creditCard.haveWalletWithProvider(request.getProvider())) {
            return ResponseEntity.unprocessableEntity()
                    .body("Cartão já está vinculado a uma carteira do provedor fornecido: " + request.getProvider());
        }

        Wallet wallet = manager.merge(request.toModel(creditCard));

        URI uri = ServletUriComponentsBuilder.fromCurrentServletMapping().pathSegment("api/wallet/{id}")
                .buildAndExpand(wallet.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @Async
    @Transactional
    @Scheduled(fixedDelay = 7000)
    @Lock(LockModeType.PESSIMISTIC_READ)
    public void submitWallets() {

        List<Wallet> unverifiedWallets = manager.createQuery("select w from Wallet w where w.status = :status", Wallet.class)
                .setParameter("status", WalletStatus.UNVERIFIED)
                .setMaxResults(5)
                .getResultList();

        unverifiedWallets.forEach(wallet -> wallet.submit(client));

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {

        Wallet wallet = manager.find(Wallet.class, id);

        if (wallet == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new WalletResponse(wallet));

    }
}
