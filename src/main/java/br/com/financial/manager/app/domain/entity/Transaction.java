package br.com.financial.manager.app.domain.entity;

import br.com.financial.manager.app.domain.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "transactions")

@NoArgsConstructor
@Getter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Column(name = "date_time", nullable = false)
    private Instant date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private Transaction(Long id, String description, Instant date, TransactionType type, Account account, Category category) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.type = type;
        this.account = account;
        this.category = category;
    }

    public static Transaction create(Long id, String description, Instant date, TransactionType type, Account account, Category category){
        return new Transaction(id, description, date, type, account, category);
    }
}
