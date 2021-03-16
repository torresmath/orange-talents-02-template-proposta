package com.zup.proposal.financialproposal.creditcard.controller;

import com.zup.proposal.financialproposal.creditcard.controller.request.CreditCardTripRequest;
import com.zup.proposal.financialproposal.creditcard.model.CreditCard;
import com.zup.proposal.financialproposal.creditcard.model.CreditCardDue;
import com.zup.proposal.financialproposal.creditcard.model.CreditCardTrip;
import com.zup.proposal.financialproposal.creditcard.repository.CreditCardRepository;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditCardTripControllerTest {

    @Mock
    private RequestHeadersValidation headersValidator;

    @Mock
    private CreditCardRepository repository;

    @Autowired
    @InjectMocks
    private CreditCardTripController controller;

    private CreditCardTripRequest request;
    private CreditCard creditCard;

    @BeforeEach
    public void init() {
        request = new CreditCardTripRequest();
        ReflectionTestUtils.setField(request, "destination", "Some place");
        ReflectionTestUtils.setField(request, "endDate", LocalDate.now().plusDays(1));

        CreditCardDue due = new CreditCardDue("identifer", 30, LocalDateTime.now());
        Address addr = new Address("street", "111", "098765432");
        Proposal proposal = new Proposal("Test", "email@teste.com", "1234567", BigDecimal.TEN, addr);
        creditCard = new CreditCard("1234", "test", LocalDateTime.now(), BigDecimal.TEN, due, proposal);
    }

    @Test
    @DisplayName("Deveria salvar viagem no cartão e retornar 200")
    void test() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(creditCard));

        when(headersValidator.getHeaders())
                .thenReturn(Optional.of(Map.of(
                    "ip_address", "0.0.0.0",
                    "user_agent", "Test-Agent"
                )));

        ResponseEntity<?> response = controller.create(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Deveria retornar 404 com cartão de crédito nao encontrado")
    void test1() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        when(headersValidator.getHeaders())
                .thenReturn(Optional.of(Map.of(
                        "ip_address", "0.0.0.0",
                        "user_agent", "Test-Agent"
                )));

        ResponseEntity<?> response = controller.create(1L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Deveria retornar 422 caso cartão já possua viagens futuras")
    void test2() {

        HashSet<CreditCardTrip> trips = new HashSet<>();
        trips.add(new CreditCardTrip("d", LocalDate.now().plusDays(1), "0.0.0.0", "Test-Agent", creditCard));

        ReflectionTestUtils.setField(creditCard, "trips", trips);

        when(repository.findById(1L))
                .thenReturn(Optional.of(creditCard));

        when(headersValidator.getHeaders())
                .thenReturn(Optional.of(Map.of(
                        "ip_address", "0.0.0.0",
                        "user_agent", "Test-Agent"
                )));

        ResponseEntity<?> response = controller.create(1L, request);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        verify(repository, times(0)).save(any());
    }
}