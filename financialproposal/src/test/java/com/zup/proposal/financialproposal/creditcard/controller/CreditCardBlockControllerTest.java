package com.zup.proposal.financialproposal.creditcard.controller;

import com.zup.proposal.financialproposal.creditcard.model.BlockStatus;
import com.zup.proposal.financialproposal.creditcard.model.CreditCard;
import com.zup.proposal.financialproposal.creditcard.model.CreditCardBlock;
import com.zup.proposal.financialproposal.creditcard.model.CreditCardDue;
import com.zup.proposal.financialproposal.proposal.model.Address;
import com.zup.proposal.financialproposal.proposal.model.Proposal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditCardBlockControllerTest {

    @Mock
    private BlockRequestHeaders headers;

    @Mock
    private EntityManager manager;

    @InjectMocks
    @Autowired
    private CreditCardBlockController controller;

    private CreditCard creditCard;
    private CreditCardBlock block;
    private HashSet<CreditCardBlock> blocks;

    @BeforeEach
    public void init() {
        CreditCardDue due = new CreditCardDue("identifer", 30, LocalDateTime.now());
        Address addr = new Address("street", "111", "098765432");
        Proposal proposal = new Proposal("Test", "email@teste.com", "1234567", BigDecimal.TEN, addr);
        creditCard = new CreditCard("1234", "test", LocalDateTime.now(), BigDecimal.TEN, due, proposal);
        block = new CreditCardBlock("0.0.0.0", "TestAgent", creditCard);
        blocks = new HashSet<>();
        blocks.add(block);
        ReflectionTestUtils.setField(creditCard, "blocks", blocks);
    }

    @Test
    @DisplayName("Deveria retornar 400 com Ip invalido")
    void test() {

        when(headers.getClientIp())
                .thenReturn(null);

        ResponseEntity<?> response = controller.block(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(manager, times(0)).find(any(), anyLong());

    }

    @Test
    @DisplayName("Deveria retornar 400 com User-Agent invalido")
    void test1() {

        when(headers.getClientIp())
                .thenReturn("0.0.0.0");

        when(headers.getUserAgent())
                .thenReturn(null);

        ResponseEntity<?> response = controller.block(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(manager, times(0)).find(any(), anyLong());
    }

    @Test
    @DisplayName("Deveria retornar 404 com cartão nao encontrado")
    void test2() {
        when(headers.getClientIp())
                .thenReturn("0.0.0.0");

        when(headers.getUserAgent())
                .thenReturn("TestAgent");

        when(manager.find(CreditCard.class, 1L))
                .thenReturn(null);

        ResponseEntity<?> response = controller.block(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Deveria retornar 422 caso cartão já esteja bloqueado")
    void test3() {
        when(headers.getClientIp())
                .thenReturn("0.0.0.0");

        when(headers.getUserAgent())
                .thenReturn("TestAgent");

        HashSet<CreditCardBlock> blocks = new HashSet<>();
        ReflectionTestUtils.setField(block, "status", BlockStatus.ACTIVE);
        blocks.add(block);
        ReflectionTestUtils.setField(creditCard, "blocks", blocks);

        when(manager.find(CreditCard.class, 1L))
                .thenReturn(creditCard);

        ResponseEntity<?> response = controller.block(1L);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("Cartão já está bloqueado", response.getBody());
    }

    @Test
    @DisplayName("Deveria retornar 422 caso já exista bloqueio em andamento")
    void test5() {
        when(headers.getClientIp())
                .thenReturn("0.0.0.0");

        when(headers.getUserAgent())
                .thenReturn("TestAgent");

        ReflectionTestUtils.setField(block, "status", BlockStatus.SCHEDULED);
        
        when(manager.find(CreditCard.class, 1L))
                .thenReturn(creditCard);

        ResponseEntity<?> response = controller.block(1L);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("Já existe um bloqueio em andamento para este cartão", response.getBody());
    }

    @Test
    @DisplayName("Deveria retornar 200 com cartão elegível para bloqueio")
    void test4() {
        when(headers.getClientIp())
                .thenReturn("0.0.0.0");

        when(headers.getUserAgent())
                .thenReturn("TestAgent");

        ReflectionTestUtils.setField(block, "status", BlockStatus.INACTIVE);
        ReflectionTestUtils.setField(creditCard, "blocks", blocks);

        when(manager.find(CreditCard.class, 1L))
                .thenReturn(creditCard);

        ResponseEntity<?> response = controller.block(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}