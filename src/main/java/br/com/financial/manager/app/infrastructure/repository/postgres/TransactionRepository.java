package br.com.financial.manager.app.infrastructure.repository.postgres;

import br.com.financial.manager.app.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
