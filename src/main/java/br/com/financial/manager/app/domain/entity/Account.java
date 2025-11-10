package br.com.financial.manager.app.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")

@NoArgsConstructor
@Getter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, scale = 2, precision = 19)
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users owner;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;

    private Account(Long id, String name, BigDecimal balance, Users owner) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.owner = owner;
        this.transactions = new ArrayList<>();
    }

    public static Account create(Long id, String name, BigDecimal balance, Users owner){
        return new Account(id, name, balance, owner);
    }
}
