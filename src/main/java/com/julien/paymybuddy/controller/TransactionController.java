package com.julien.paymybuddy.controller;

import com.julien.paymybuddy.dto.TransactionDTO;
import com.julien.paymybuddy.entity.UserEntity;
import com.julien.paymybuddy.exception.UnauthorizedRecipientException;
import com.julien.paymybuddy.exception.UserNotFoundException;
import com.julien.paymybuddy.service.TransactionService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @GetMapping("/find-all")
    public ResponseEntity<List<TransactionDTO>> getCurrentUserTransactions(HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("user");

        if (currentUser == null) {
            logger.error("No current user.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            List<TransactionDTO> transactionsList = transactionService.findAllTransactionsByUserId(currentUser.getUserId());
            if (null != transactionsList) {
                logger.info("Found {} transactions.", transactionsList.size());
                return ResponseEntity.ok(transactionsList);
            } else {
                logger.error("No transactions found.");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Unknown error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO transactionDTO, HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("user");

        if (currentUser == null) {
            logger.error("No current user.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            transactionService.createTransaction(currentUser, transactionDTO);
            logger.info("Transaction successfully created.");
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedRecipientException e) {
            logger.error("Unauthorized recipient: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.error("Unknown error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
