package com.julien.paymybuddy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "userId")
@ToString(exclude = {"sentTransactions", "receivedTransactions", "connectionsInitiated"})
@Generated
@Entity
@Table(name="User")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

    @Column(nullable = false, name = "username")
    private String username;

    @Column(nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

    @OneToMany(mappedBy = "sender")
    private Set<TransactionEntity> sentTransactions;

    @OneToMany(mappedBy = "receiver")
    private Set<TransactionEntity> receivedTransactions;

    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL)
    private Set<UserConnectionEntity> connectionsInitiated;
}
