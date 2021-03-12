package com.zup.proposal.financialproposal.proposal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.proposal.financialproposal.builders.MockMvcBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

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
    @DisplayName("Deveria encontrar Proposta e retornar 200")
    @WithMockUser
    void deveriaEncontrarRetornar200() throws Exception {

        new MockMvcBuilder().get("/api/proposal/1", 200, mockMvc);
    }

    @Test
    @DisplayName("Deveria retornar 404 caso nao encontre Proposta")
    @WithMockUser
    void deveriaRetornar404NotFound() throws Exception {
        new MockMvcBuilder().get("/api/proposal/99999", 404, mockMvc);
    }

}