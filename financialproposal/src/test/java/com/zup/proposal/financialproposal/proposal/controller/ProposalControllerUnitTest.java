package com.zup.proposal.financialproposal.proposal.controller;

import com.zup.proposal.financialproposal.client.analysis.AnalysisClient;
import com.zup.proposal.financialproposal.client.analysis.response.AnalysisResponse;
import com.zup.proposal.financialproposal.client.analysis.response.AnalysisResult;
import com.zup.proposal.financialproposal.proposal.controller.request.AddressRequest;
import com.zup.proposal.financialproposal.proposal.controller.request.ProposalRequest;
import com.zup.proposal.financialproposal.proposal.model.Proposal;
import com.zup.proposal.financialproposal.proposal.repository.ProposalRepository;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProposalControllerUnitTest {

    @Autowired
    @InjectMocks
    private ProposalController controller;

    @Mock
    private ProposalRepository repository;

    @Mock
    private AnalysisClient client;

    private ProposalRequest request;

    @BeforeEach
    public void init() {

        AddressRequest addr = new AddressRequest("Rua", "111", "1234567");
        request = new ProposalRequest("Matheus", "email@email.com", "12345678", BigDecimal.ONE, addr);
    }

    @Test
    @DisplayName("Deveria criar proposta sem restrição")
    void deveriaCriarProposta() {



        Proposal proposal = request.toModel();
        when(repository.existsByDocument(request.getDocument()))
                .thenReturn(false);

        when(repository.save(any()))
                .thenReturn(proposal);

        when(client.analysisProposalRequest(any()))
                .thenReturn(new AnalysisResponse(AnalysisResult.SEM_RESTRICAO));

        MockHttpServletRequest servlet = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servlet));

        ResponseEntity<?> response = controller.create(request);

        verify(repository, times(1)).save(any(Proposal.class));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Deveria criar proposta com restrição")
    void deveriaCriarPropostaComRestricao() {

        Proposal proposal = request.toModel();
        when(repository.existsByDocument(request.getDocument()))
                .thenReturn(false);

        when(repository.save(any()))
                .thenReturn(proposal);

        when(client.analysisProposalRequest(any()))
                .thenReturn(new AnalysisResponse(AnalysisResult.COM_RESTICAO));

        MockHttpServletRequest servlet = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servlet));

        ResponseEntity<?> response = controller.create(request);

        verify(repository, times(1)).save(any(Proposal.class));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Não deveria criar proposta duplicada")
    void naoDeveriaCriarDuplicada() {

        when(repository.existsByDocument(request.getDocument()))
                .thenReturn(true);

        ResponseEntity<?> response = controller.create(request);

        verify(repository, times(0)).save(any());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

}