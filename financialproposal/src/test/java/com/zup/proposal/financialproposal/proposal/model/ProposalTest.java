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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProposalTest {

    @Mock
    private AccountClient client;

    @Mock
    private ProposalRepository repository;

    @Mock
    private EntityManager manager;

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
        // WHEN
        when(client.requestCreditCard(any()))
                .thenReturn(Optional.of(response));

        // GIVEN
        proposal.generateCreditCard(client, repository);

        ArgumentCaptor<ProposalApiRequest> captor = ArgumentCaptor.forClass(ProposalApiRequest.class);

        // THEN
        verify(client, times(1)).requestCreditCard(captor.capture());
        verify(repository, times(1)).save(any(Proposal.class));
        assertEquals(proposal.getId(), captor.getValue().getIdProposta());
    }

    @Test
    @DisplayName("Não deveria salvar em caso de falha no client")
    void naoDeveriaSalvarClientErro() {
        // WHEN
        when(client.requestCreditCard(any()))
                .thenReturn(Optional.empty());

        // GIVEN
        proposal.generateCreditCard(client, repository);

        // THEN
        verify(repository, times(0)).save(any(Proposal.class));
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