package com.zup.proposal.financialproposal.wallet.controller;

import com.zup.proposal.financialproposal.creditcard.model.CreditCard;
import com.zup.proposal.financialproposal.creditcard.model.CreditCardDue;
import com.zup.proposal.financialproposal.proposal.model.Address;
import com.zup.proposal.financialproposal.proposal.model.Proposal;
import com.zup.proposal.financialproposal.wallet.controller.request.WalletRequest;
import com.zup.proposal.financialproposal.wallet.model.Provider;
import com.zup.proposal.financialproposal.wallet.model.Wallet;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    @Mock
    private EntityManager manager;

    @Autowired
    @InjectMocks
    private WalletController controller;

    private CreditCard creditCard;
    private WalletRequest request;

    @BeforeEach
    public void init() {
        CreditCardDue due = new CreditCardDue("identifer", 30, LocalDateTime.now());
        Address addr = new Address("street", "111", "098765432");
        Proposal proposal = new Proposal("Test", "email@teste.com", "1234567", BigDecimal.TEN, addr);
        creditCard = new CreditCard("1234", "test", LocalDateTime.now(), BigDecimal.TEN, due, proposal);

        request = new WalletRequest();
        ReflectionTestUtils.setField(request, "email", "email@teste.com");
        ReflectionTestUtils.setField(request, "provider", "PAYPAL");
    }

    @Test
    @DisplayName("Deveria criar carteira e retornar 201")
    void test() {

        Wallet wallet = new Wallet("email", Provider.PAYPAL, creditCard);
        ReflectionTestUtils.setField(wallet, "id", 1L);

        when(manager.find(CreditCard.class, 1L))
                .thenReturn(creditCard);

        when(manager.merge(any(Wallet.class)))
                .thenReturn(wallet);

        MockHttpServletRequest mockServlet = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockServlet));

        ResponseEntity<?> response = controller.create(1L, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Deveria retornar 404 caso cartão não exista")
    void test1() {
        when(manager.find(CreditCard.class, 1L))
                .thenReturn(null);

        ResponseEntity<?> response = controller.create(1L, request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(manager, times(0)).merge(any(Wallet.class));
    }

    @Test
    @DisplayName("Deveria retornar 422 caso cartão já esteja vinculado a carteira com Provider fornecido")
    void test2() {

        HashSet<Wallet> wallets = new HashSet<>();
        wallets.add(new Wallet("email@teste.com", Provider.PAYPAL, creditCard));

        ReflectionTestUtils.setField(creditCard, "wallets", wallets);

        when(manager.find(CreditCard.class, 1L))
                .thenReturn(creditCard);

        ResponseEntity<?> response = controller.create(1L, request);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        verify(manager, times(0)).merge(any(Wallet.class));
    }
}