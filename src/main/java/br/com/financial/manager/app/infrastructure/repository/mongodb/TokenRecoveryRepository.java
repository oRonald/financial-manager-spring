package br.com.financial.manager.app.infrastructure.repository.mongodb;

import br.com.financial.manager.app.domain.entity.token.TokenRecovery;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TokenRecoveryRepository extends MongoRepository<TokenRecovery, String> {

    void deleteAllByUserEmailAndIsUsedFalse(String userEmail);
    Optional<TokenRecovery> findByToken(String token);
}
