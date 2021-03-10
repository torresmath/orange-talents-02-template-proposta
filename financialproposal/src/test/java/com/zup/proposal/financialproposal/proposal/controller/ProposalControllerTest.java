package com.zup.proposal.financialproposal.proposal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.proposal.financialproposal.builders.MockMvcBuilder;
import com.zup.proposal.financialproposal.proposal.controller.request.AddressRequest;
import com.zup.proposal.financialproposal.proposal.controller.request.ProposalRequest;
import com.zup.proposal.financialproposal.proposal.model.Proposal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProposalControllerTest {

    @Autowired
    private EntityManager manager;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deveria retornar 201")
    void deveriaRetornar201() throws Exception {

        AddressRequest address = new AddressRequest("Rua", "111", "08572330");
        ProposalRequest request = new ProposalRequest("matheus", "email@teste.com", "56568071005", BigDecimal.valueOf(1000L), address);

        ResultActions perform = new MockMvcBuilder().perform("/api/proposal", request, 201, mockMvc, mapper);

        String location = perform.andReturn().getResponse().getHeader("Location");

        Proposal proposal = manager.find(Proposal.class, 2L);

        assertEquals("http://localhost/api/proposal/2", location);
        assertNotNull(proposal);
        assertEquals(request.getName(), proposal.getName());
        assertEquals(address.getNumber(), proposal.getNumber());
    }

    @Test
    @DisplayName("Deveria retornar 422")
    void deveriaRetornar422() throws Exception {

        AddressRequest address = new AddressRequest("Rua", "111", "08572330");
        ProposalRequest request = new ProposalRequest("matheus", "email@teste.com", "425.497.898-73", BigDecimal.valueOf(1000L), address);

        new MockMvcBuilder().perform("/api/proposal", request, 422, mockMvc, mapper);

        Proposal proposal = manager.find(Proposal.class, 2L);

        assertNull(proposal);
    }

    @Test
    @DisplayName("Deveria encontrar Proposta e retornar 200")
    void deveriaEncontrarRetornar200() throws Exception {

        new MockMvcBuilder().get("/api/proposal/1", 200, mockMvc);
    }

    @Test
    @DisplayName("Deveria retornar 404 caso nao encontre Proposta")
    void deveriaRetornar404NotFound() throws Exception {
        new MockMvcBuilder().get("/api/proposal/99999", 404, mockMvc);
    }

}