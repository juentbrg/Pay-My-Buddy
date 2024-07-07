package com.julien.paymybuddy.dto;

import com.julien.paymybuddy.entity.TransactionEntity;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Generated
public class TransactionDTO {
    private String relationName;
    private String description;
    private BigDecimal amount;

    public TransactionDTO(TransactionEntity transactionEntity) {
        this.amount = transactionEntity.getAmount();
        this.description = transactionEntity.getDescription();
        this.relationName = transactionEntity.getReceiver().getUsername();
    }
}
