package com.zup.proposal.financialproposal.biometry.model;

import com.zup.proposal.financialproposal.proposal.model.Address;
import com.zup.proposal.financialproposal.proposal.model.CreditCard;
import com.zup.proposal.financialproposal.proposal.model.CreditCardDue;
import com.zup.proposal.financialproposal.proposal.model.Proposal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BiometryTest {

    private CreditCard creditCard;

    @BeforeEach
    public void init() {

        CreditCardDue creditCardDue = new CreditCardDue("123456789", 30, LocalDateTime.now());
        Address addr = new Address("Rua", "123456", "12345678");
        Proposal proposal = new Proposal("Teste", "email@teste.com", "123456789", BigDecimal.ONE, addr);
        creditCard = new CreditCard("123456", "Teste", LocalDateTime.now(), BigDecimal.TEN, creditCardDue, proposal);
    }

    @Test
    @DisplayName("Deveria criar biometria com Base64 válida")
    void test() {
        assertDoesNotThrow(() -> new Biometry("abcdefgh", creditCard));
    }

    @Test
    @DisplayName("Deveria retornar erro ao criar biometria com Base64 inválida")
    void test1() {
        assertThrows(IllegalStateException.class, () -> new Biometry("bbbbb", creditCard));
    }

    @Test
    @DisplayName("Deveria retornar erro ao criar biometria sem cartão")
    void test2() {
        assertThrows(IllegalArgumentException.class, () -> new Biometry("abcdefgh", null));
    }

}