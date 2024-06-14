package com.julien.paymybuddy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "userId")
@ToString(exclude = {"sentTransactions", "receivedTransactions", "connectionsInitiated", "connectionsReceived"})
@Entity
@Table(name="User")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

    @NotEmpty(message = "Username cannot be empty")
    @Column(nullable = false, name = "username")
    private String username;

    @NotEmpty(message = "Username cannot be empty")
    @Email(message = "Email should be valid")
    @Column(nullable = false, name = "email")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Column(nullable = false, name = "password")
    private String password;

    @OneToMany(mappedBy = "sender")
    private Set<TransactionEntity> sentTransactions;

    @OneToMany(mappedBy = "receiver")
    private Set<TransactionEntity> receivedTransactions;

    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL)
    private Set<UserConnectionEntity> connectionsInitiated;

    @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL)
    private Set<UserConnectionEntity> connectionsReceived;
}
