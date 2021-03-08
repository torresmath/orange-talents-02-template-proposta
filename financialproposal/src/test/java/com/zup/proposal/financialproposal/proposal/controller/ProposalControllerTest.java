package com.zup.proposal.financialproposal.proposal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.proposal.financialproposal.builders.MockMvcBuilder;
import com.zup.proposal.financialproposal.proposal.controller.request.AddressRequest;
import com.zup.proposal.financialproposal.proposal.controller.request.ProposalRequest;
import com.zup.proposal.financialproposal.proposal.model.Proposal;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void deveriaRetornar201() throws Exception {

        AddressRequest address = new AddressRequest("Rua", "111", "08572330");
        ProposalRequest request = new ProposalRequest("matheus", "email@teste.com", "42549789873", BigDecimal.valueOf(1000L), address);

        ResultActions perform = new MockMvcBuilder().perform("/api/proposal", request, 201, mockMvc, mapper);

        String location = perform.andReturn().getResponse().getHeader("Location");

        Proposal proposal = manager.find(Proposal.class, 1L);

        assertEquals("http://localhost/api/proposal/1", location);
        assertNotNull(proposal);
        assertEquals(request.getName(), proposal.getName());
        assertEquals(address.getNumber(), proposal.getNumber());
    }

}