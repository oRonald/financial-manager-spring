package br.com.financial.manager.app.infrastructure.repository.postgres;

import br.com.financial.manager.app.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
