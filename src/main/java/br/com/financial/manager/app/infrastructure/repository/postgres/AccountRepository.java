package br.com.financial.manager.app.infrastructure.repository.postgres;

import br.com.financial.manager.app.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByNameAndOwnerId(String accountName, Long userId);
    Optional<Account> findByNameAndOwnerId(String AccountName, Long userId);
}
