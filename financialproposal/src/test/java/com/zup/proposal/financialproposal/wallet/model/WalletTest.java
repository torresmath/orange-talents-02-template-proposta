package com.zup.proposal.financialproposal.wallet.model;

import com.zup.proposal.financialproposal.creditcard.model.CreditCard;
import com.zup.proposal.financialproposal.creditcard.model.CreditCardDue;
import com.zup.proposal.financialproposal.proposal.model.Address;
import com.zup.proposal.financialproposal.proposal.model.Proposal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WalletTest {

    private CreditCard creditCard;

    @BeforeEach
    public void init() {
        CreditCardDue due = new CreditCardDue("identifer", 30, LocalDateTime.now());
        Address addr = new Address("street", "111", "098765432");
        Proposal proposal = new Proposal("Test", "email@teste.com", "1234567", BigDecimal.TEN, addr);
        creditCard = new CreditCard("1234", "test", LocalDateTime.now(), BigDecimal.TEN, due, proposal);

    }

    @Test
    @DisplayName("Deveria retornar erro caso cartão já esteja vinculado")
    void test() {

        HashSet<Wallet> wallets = new HashSet<>();
        wallets.add(new Wallet("email@teste.com", Provider.PAYPAL, creditCard));
        ReflectionTestUtils.setField(creditCard, "wallets", wallets);

        assertThrows(IllegalStateException.class, () -> new Wallet("email@teste.com", Provider.PAYPAL, creditCard));
    }

    @Test
    @DisplayName("Deveria criar carteira com sucesso")
    void test1() {

        HashSet<Wallet> wallets = new HashSet<>();
        ReflectionTestUtils.setField(creditCard, "wallets", wallets);

        assertDoesNotThrow(() -> new Wallet("email@teste.com", Provider.PAYPAL, creditCard));
    }

    @Test
    @DisplayName("Deveria criar carteira com sucesso caso cartao esteja vinculado a carteira de provedor DIFERENTE")
    void test2() {

        HashSet<Wallet> wallets = new HashSet<>();
        wallets.add(new Wallet("email@teste.com", Provider.PAYPAL, creditCard));
        ReflectionTestUtils.setField(creditCard, "wallets", wallets);

        assertDoesNotThrow(() -> new Wallet("email@teste.com", Provider.SAMSUNG_PAY, creditCard));
    }

}