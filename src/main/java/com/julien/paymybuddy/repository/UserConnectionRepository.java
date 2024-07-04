package com.julien.paymybuddy.repository;

import com.julien.paymybuddy.entity.UserConnectionEntity;
import com.julien.paymybuddy.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnectionEntity, Long> {
    Optional<UserConnectionEntity> findByUser1AndUser2(UserEntity user1, UserEntity user2);

    @Query("SELECT uc FROM UserConnectionEntity uc JOIN FETCH uc.user1 JOIN FETCH uc.user2 WHERE uc.user1 = :user1")
    List<UserConnectionEntity> findByUser1WithUsers(@Param("user1") UserEntity user1);

    @Query("SELECT uc.user2.username FROM UserConnectionEntity uc WHERE uc.user1 = :user1")
    List<String> findUserNamesOfConnections(@Param("user1") UserEntity user1);
}
