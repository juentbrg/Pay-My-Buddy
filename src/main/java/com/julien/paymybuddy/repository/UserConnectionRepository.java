package com.julien.paymybuddy.repository;

import com.julien.paymybuddy.entity.UserConnectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnectionEntity, Long> {
}
