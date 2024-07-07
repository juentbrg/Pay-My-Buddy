package com.julien.paymybuddy.service;

import com.julien.paymybuddy.dto.TransactionDTO;
import com.julien.paymybuddy.entity.TransactionEntity;
import com.julien.paymybuddy.entity.UserConnectionEntity;
import com.julien.paymybuddy.entity.UserEntity;
import com.julien.paymybuddy.exception.UnauthorizedRecipientException;
import com.julien.paymybuddy.exception.UserNotFoundException;
import com.julien.paymybuddy.repository.TransactionRepository;
import com.julien.paymybuddy.repository.UserConnectionRepository;
import com.julien.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    private static AutoCloseable mocks;

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserConnectionRepository userConnectionRepository;

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
    public void findAllTransactionsByUserIdOkTest() {
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

        when(transactionRepository.findAllByUserID(1)).thenReturn(transactions);

        List<TransactionDTO> result = transactionService.findAllTransactionsByUserId(1);

        assertEquals(transactions.stream().map(TransactionDTO::new).toList(), result);
    }

    @Test
    public void findAllTransactionsByUserIdWithNullList() {
        when(transactionRepository.findAllByUserID(1)).thenReturn(Collections.emptyList());

        List<TransactionDTO> result = transactionService.findAllTransactionsByUserId(1);

        assertNull(result);
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

        TransactionEntity transactionEntity = new TransactionEntity(0, userJohn, userJane, "test", BigDecimal.valueOf(45));
        TransactionDTO transactionDTO = new TransactionDTO(transactionEntity);

        when(userRepository.findByUsername(userJane.getUsername())).thenReturn(Optional.of(userJane));
        when(userConnectionRepository.findByUser1AndUser2(userJohn, userJane)).thenReturn(Optional.of(new UserConnectionEntity()));

        transactionService.createTransaction(userJohn, transactionDTO);

        verify(transactionRepository, times(1)).save(transactionEntity);
    }

    @Test
    public void createTransactionWithNullReceiverTest() {
        UserEntity userJohn = new UserEntity();
        UserEntity userJane = new UserEntity();
        TransactionDTO transactionDTO = new TransactionDTO(new TransactionEntity(0, userJohn, userJane, "test", BigDecimal.valueOf(45)));

        when(userRepository.findByUsername("Jane")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            transactionService.createTransaction(userJohn, transactionDTO);
        });
    }

    @Test
    public void createTransactionWithNoRelationTest() {
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

        TransactionEntity transactionEntity = new TransactionEntity(0, userJohn, userJane, "test", BigDecimal.valueOf(45));
        TransactionDTO transactionDTO = new TransactionDTO(transactionEntity);

        when(userRepository.findByUsername(userJane.getUsername())).thenReturn(Optional.of(userJane));
        when(userConnectionRepository.findByUser1AndUser2(userJohn, userJane)).thenReturn(Optional.empty());

        assertThrows(UnauthorizedRecipientException.class, () -> {
            transactionService.createTransaction(userJohn, transactionDTO);
        });
    }
}
