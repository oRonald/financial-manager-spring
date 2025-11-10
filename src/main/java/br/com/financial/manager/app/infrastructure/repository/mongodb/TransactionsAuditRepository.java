package br.com.financial.manager.app.infrastructure.repository.mongodb;

import br.com.financial.manager.app.domain.entity.audit.TransactionsAudit;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionsAuditRepository extends MongoRepository<TransactionsAudit, Long> {
}
