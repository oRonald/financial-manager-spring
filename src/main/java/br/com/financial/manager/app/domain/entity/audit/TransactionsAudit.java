package br.com.financial.manager.app.domain.entity.audit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;

@Document(collection = "transactionAudit")
@Getter
@NoArgsConstructor
public class TransactionsAudit {

    @Id
    private Long id;

    @Field(name = "user_id")
    private Long userId;

    @Field(name = "transaction_time")
    private Instant transactionTime;

    @Field(name = "period_start")
    private LocalDate periodStart;

    private TransactionsAudit(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
        this.transactionTime = Instant.now();
        this.periodStart = LocalDate.now();
    }

    public static TransactionsAudit create(Long id, Long userId){
        return new TransactionsAudit(id, userId);
    }
}
