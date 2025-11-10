package br.com.financial.manager.app.infrastructure.repository.postgres;

import br.com.financial.manager.app.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
