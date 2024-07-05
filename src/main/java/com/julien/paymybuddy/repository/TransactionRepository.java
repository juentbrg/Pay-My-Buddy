package com.julien.paymybuddy.repository;

import com.julien.paymybuddy.entity.TransactionEntity;
import com.julien.paymybuddy.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("select t from TransactionEntity t where t.sender.userId = ?1")
    public List<TransactionEntity> findAllByUserID(long userId);
}
