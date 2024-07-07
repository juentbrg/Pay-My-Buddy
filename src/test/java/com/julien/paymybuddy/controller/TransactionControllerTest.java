package com.julien.paymybuddy.controller;

import com.julien.paymybuddy.dto.TransactionDTO;
import com.julien.paymybuddy.entity.TransactionEntity;
import com.julien.paymybuddy.entity.UserEntity;
import com.julien.paymybuddy.exception.UnauthorizedRecipientException;
import com.julien.paymybuddy.exception.UserNotFoundException;
import com.julien.paymybuddy.service.TransactionService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    private static AutoCloseable mocks;

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    @Mock
    private HttpSession session;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterAll
    public static void tearDown() throws Exception {
        if (null != mocks) {
            mocks.close();
        }
    }

    @Test
    public void getCurrentUserTransactionsTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        UserEntity userJane = new UserEntity();
        userJane.setUserId(2);
        userJane.setUsername("Jane");
        userJane.setPassword("password");
        userJane.setEmail("jane.doe@mail.com");

        List<TransactionEntity> transactions = new ArrayList<>();
        transactions.add(new TransactionEntity(1, userJohn, userJane, "test", BigDecimal.valueOf(45)));
        transactions.add(new TransactionEntity(1, userJane, userJohn, "blop", BigDecimal.valueOf(54)));

        when(session.getAttribute("user")).thenReturn(userJohn);
        when(transactionService.findAllTransactionsByUserId(1)).thenReturn(transactions.stream().map(TransactionDTO::new).toList());

        ResponseEntity<List<TransactionDTO>> result = transactionController.getCurrentUserTransactions(session);

        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void getCurrentUserTransactionsUnauthorizedTest() {
        when(session.getAttribute("user")).thenReturn(null);

        ResponseEntity<List<TransactionDTO>> result = transactionController.getCurrentUserTransactions(session);

        assertEquals(result.getStatusCode().value(), 401);
    }

    @Test
    public void getCurrentUserTransactionsWithNoTransactionsTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        when(session.getAttribute("user")).thenReturn(userJohn);
        when(transactionService.findAllTransactionsByUserId(1)).thenReturn(null);

        ResponseEntity<List<TransactionDTO>> result = transactionController.getCurrentUserTransactions(session);

        assertEquals(result.getStatusCode().value(), 404);
    }

    @Test
    public void getCurrentUserTransactionsUnknownErrorTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        when(session.getAttribute("user")).thenReturn(userJohn);
        when(transactionService.findAllTransactionsByUserId(1)).thenThrow(new RuntimeException());

        ResponseEntity<List<TransactionDTO>> result = transactionController.getCurrentUserTransactions(session);

        assertEquals(result.getStatusCode().value(), 500);
    }

    @Test
    public void createTransactionOkTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        UserEntity userJane = new UserEntity();
        userJane.setUserId(2);
        userJane.setUsername("Jane");
        userJane.setPassword("password");
        userJane.setEmail("jane.doe@mail.com");

        TransactionDTO transactionDTO = new TransactionDTO(new TransactionEntity(1, userJohn, userJane, "test", BigDecimal.valueOf(45)));

        when(session.getAttribute("user")).thenReturn(userJohn);
        doNothing().when(transactionService).createTransaction(userJohn, transactionDTO);

        ResponseEntity<?> result = transactionController.createTransaction(transactionDTO, session);

        assertEquals(result.getStatusCode().value(), 200);
    }

    @Test
    public void createTransactionUnauthorizedTest() {
        UserEntity userJohn = new UserEntity();
        UserEntity userJane = new UserEntity();
        TransactionDTO transactionDTO = new TransactionDTO(new TransactionEntity(1, userJohn, userJane, "test", BigDecimal.valueOf(45)));

        when(session.getAttribute("user")).thenReturn(null);

        ResponseEntity<?> result = transactionController.createTransaction(transactionDTO, session);

        assertEquals(result.getStatusCode().value(), 401);
    }

    @Test
    public void createTransactionUserNotFoundTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        UserEntity userJane = new UserEntity();

        TransactionDTO transactionDTO = new TransactionDTO(new TransactionEntity(1, userJohn, userJane, "test", BigDecimal.valueOf(45)));

        when(session.getAttribute("user")).thenReturn(userJohn);
        doThrow(new UserNotFoundException()).when(transactionService).createTransaction(userJohn, transactionDTO);

        ResponseEntity<?> result = transactionController.createTransaction(transactionDTO, session);

        assertEquals(result.getStatusCode().value(), 404);
    }

    @Test
    public void createTransactionUnauthorizedRecipientTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        UserEntity userJane = new UserEntity();
        userJane.setUserId(2);
        userJane.setUsername("Jane");
        userJane.setPassword("password");
        userJane.setEmail("jane.doe@mail.com");

        TransactionDTO transactionDTO = new TransactionDTO(new TransactionEntity(1, userJohn, userJane, "test", BigDecimal.valueOf(45)));

        when(session.getAttribute("user")).thenReturn(userJohn);
        doThrow(new UnauthorizedRecipientException()).when(transactionService).createTransaction(userJohn, transactionDTO);

        ResponseEntity<?> result = transactionController.createTransaction(transactionDTO, session);

        assertEquals(result.getStatusCode().value(), 401);
    }

    @Test
    public void createTransactionUnknownErrorTest() {
        UserEntity userJohn = new UserEntity();
        userJohn.setUserId(1);
        userJohn.setUsername("John");
        userJohn.setPassword("password");
        userJohn.setEmail("john.doe@mail.com");

        UserEntity userJane = new UserEntity();
        userJane.setUserId(2);
        userJane.setUsername("Jane");
        userJane.setPassword("password");
        userJane.setEmail("jane.doe@mail.com");

        TransactionDTO transactionDTO = new TransactionDTO(new TransactionEntity(1, userJohn, userJane, "test", BigDecimal.valueOf(45)));

        when(session.getAttribute("user")).thenReturn(userJohn);
        doThrow(new RuntimeException()).when(transactionService).createTransaction(userJohn, transactionDTO);

        ResponseEntity<?> result = transactionController.createTransaction(transactionDTO, session);

        assertEquals(result.getStatusCode().value(), 500);
    }
}
