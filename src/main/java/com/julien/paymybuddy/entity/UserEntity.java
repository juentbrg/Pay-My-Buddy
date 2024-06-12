package com.julien.paymybuddy.entity;

import jakarta.persistence.*;
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
    public long userId;

    @Column(name = "username")
    public String username;

    @Column(name = "email")
    public String email;

    @Column(name = "password")
    public String password;

    @OneToMany(mappedBy = "sender")
    private Set<TransactionEntity> sentTransactions;

    @OneToMany(mappedBy = "receiver")
    private Set<TransactionEntity> receivedTransactions;

    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL)
    private Set<UserConnectionEntity> connectionsInitiated;

    @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL)
    private Set<UserConnectionEntity> connectionsReceived;
}
