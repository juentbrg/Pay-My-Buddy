package com.julien.paymybuddy.service;

import com.julien.paymybuddy.dto.TransactionDTO;
import com.julien.paymybuddy.entity.TransactionEntity;
import com.julien.paymybuddy.entity.UserEntity;
import com.julien.paymybuddy.exception.UnauthorizedRecipientException;
import com.julien.paymybuddy.exception.UserNotFoundException;
import com.julien.paymybuddy.repository.TransactionRepository;
import com.julien.paymybuddy.repository.UserConnectionRepository;
import com.julien.paymybuddy.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final UserConnectionRepository userConnectionRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, UserConnectionRepository userConnectionRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.userConnectionRepository = userConnectionRepository;
    }

    @Transactional(readOnly = true)
    public List<TransactionDTO> findAllTransactionsByUserId(long id) {
        List<TransactionEntity> transactionList = transactionRepository.findAllByUserID(id);
        return transactionList.stream()
                .map(TransactionDTO::new)
                .toList();
    }

    @Transactional
    public void createTransaction(UserEntity currentUser, TransactionDTO transactionDTO) {
        Optional<UserEntity> receiverOpt = userRepository.findByUsername(transactionDTO.getRelationName());

        if (receiverOpt.isPresent()) {
            UserEntity receiver = receiverOpt.get();
            if (userConnectionRepository.findByUser1AndUser2(currentUser, receiver).isPresent()) {
                TransactionEntity transactionEntity = new TransactionEntity();
                transactionEntity.setSender(currentUser);
                transactionEntity.setReceiver(receiver);
                transactionEntity.setDescription(transactionDTO.getDescription());
                transactionEntity.setAmount(transactionDTO.getAmount());

                transactionRepository.save(transactionEntity);
            } else {
                throw new UnauthorizedRecipientException();
            }

        } else {
            throw new UserNotFoundException();
        }
    }
}
