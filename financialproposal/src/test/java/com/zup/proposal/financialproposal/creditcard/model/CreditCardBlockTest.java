package com.zup.proposal.financialproposal.creditcard.model;

import com.zup.proposal.financialproposal.client.account.AccountClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditCardBlockTest {

    @Mock
    private AccountClient client;

    private CreditCardBlock creditCardBlock;

    private CreditCardBlock block;

    @BeforeEach
    public void init() {

        block = new CreditCardBlock("0.0.0.0.1", "AgentTest", new CreditCard());

    }

    @Test
    @DisplayName("Não deveria atualizar bloqueio caso requisição falhe")
    void test() {

        when(client.blockCreditCard("12345678", Map.of("sistemaResponsavel", "AgentTest")))
                .thenReturn(Optional.empty());

        block.setAsActive(client, "12345678");

        assertEquals(BlockStatus.SCHEDULED, block.getStatus());
    }

    @Test
    @DisplayName("Não deveria atualizar bloqueio caso status não seja Scheduled")
    void test1() {
        ReflectionTestUtils.setField(block, "status", BlockStatus.ACTIVE);
        assertThrows(IllegalStateException.class, () -> block.setAsActive(client, "12345678"));

        ReflectionTestUtils.setField(block, "status", BlockStatus.INACTIVE);
        assertThrows(IllegalStateException.class, () -> block.setAsActive(client, "12345678"));

    }

}