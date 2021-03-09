package com.zup.proposal.financialproposal.proposal.model;

import com.zup.proposal.financialproposal.client.ProposalApiRequest;
import com.zup.proposal.financialproposal.client.account.AccountClient;
import com.zup.proposal.financialproposal.client.account.response.CreditCardDueResponse;
import com.zup.proposal.financialproposal.client.account.response.CreditCardResponse;
import com.zup.proposal.financialproposal.proposal.repository.ProposalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProposalTest {

    @Mock
    private AccountClient client;

    @Mock
    private ProposalRepository repository;

    private Proposal proposal;

    private CreditCardResponse response;

    @BeforeEach
    public void init() {
        proposal = new Proposal("Nome", "email@email.com", "42549789873", BigDecimal.TEN, new Address());
        CreditCardDueResponse creditCardDueResponse = new CreditCardDueResponse("12314564", 30, LocalDateTime.now());
        response = new CreditCardResponse("123456", 1L, LocalDateTime.now(), "Teste", BigDecimal.TEN, creditCardDueResponse);

    }

    @Test
    @DisplayName("Deveria salvar cartao")
    void deveriaSalvarCartao() {

        when(client.requestCreditCard(any()))
                .thenReturn(response);

        proposal.generateCreditCard(client, repository);
        verify(repository, times(1)).save(any(Proposal.class));
    }

    @Test
    @DisplayName("Não deveria salvar cartão para proposta não elegível")
    void naoDeveriaSalvarNaoElegivel() {
        ReflectionTestUtils.setField(proposal, "status", ProposalStatus.NAO_ELEGIVEL);
        proposal.generateCreditCard(client, repository);
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Não deveria salvar cartão para proposta com cartão")
    void naoDeveriaSalvarComCartao() {
        ReflectionTestUtils.setField(proposal, "creditCard", response.toCreditCard(proposal));
        proposal.generateCreditCard(client, repository);
        verifyNoInteractions(repository);
    }

}