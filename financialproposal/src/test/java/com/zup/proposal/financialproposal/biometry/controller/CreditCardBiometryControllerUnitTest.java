package com.zup.proposal.financialproposal.biometry.controller;

import com.zup.proposal.financialproposal.biometry.controller.request.BiometryRequest;
import com.zup.proposal.financialproposal.biometry.model.Biometry;
import com.zup.proposal.financialproposal.proposal.model.Address;
import com.zup.proposal.financialproposal.proposal.model.CreditCard;
import com.zup.proposal.financialproposal.proposal.model.CreditCardDue;
import com.zup.proposal.financialproposal.proposal.model.Proposal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CreditCardBiometryControllerUnitTest {

    @Autowired
    @InjectMocks
    private CreditCardBiometryController controller;

    @Mock
    private EntityManager manager;

    private CreditCard creditCard;

    private BiometryRequest request;

    @BeforeEach
    public void init() {

        CreditCardDue creditCardDue = new CreditCardDue("123456789", 30, LocalDateTime.now());
        Address addr = new Address("Rua", "123456", "12345678");
        Proposal proposal = new Proposal("Teste", "email@teste.com", "123456789", BigDecimal.ONE, addr);
        creditCard = new CreditCard("123456", "Teste", LocalDateTime.now(), BigDecimal.TEN, creditCardDue, proposal);

        request = new BiometryRequest();
        request.setFingerprint("abcdef");
    }

    @Test
    @DisplayName("Deveria salvar biometria e retornar 201")
    void test() {


        when(manager.find(CreditCard.class, 1L))
                .thenReturn(creditCard);

        Biometry savedBiometry = new Biometry("abcdef", creditCard);
        ReflectionTestUtils.setField(savedBiometry, "id", 1L);
        when(manager.merge(any(Biometry.class)))
                .thenReturn(savedBiometry);

        MockHttpServletRequest servlet = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servlet));

        ResponseEntity<?> response = controller.create(1L, request);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Deveria retornar 404 caso cartão nao exista")
    void test1() {

        when(manager.find(CreditCard.class, 1L))
                .thenReturn(null);

        ResponseEntity<?> response = controller.create(1L, request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(manager, times(0)).merge(any(Biometry.class));

    }

}