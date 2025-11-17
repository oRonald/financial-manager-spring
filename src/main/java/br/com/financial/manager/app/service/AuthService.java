package br.com.financial.manager.app.service;

import br.com.financial.manager.app.domain.entity.dto.ChangePasswordDTO;
import br.com.financial.manager.app.domain.entity.dto.LoginResponse;
import br.com.financial.manager.app.domain.entity.dto.RecoveryTokenDTO;
import br.com.financial.manager.app.infrastructure.security.jwt.UsersLoginDTO;
import jakarta.validation.Valid;

public interface AuthService {

    LoginResponse login(UsersLoginDTO dto);
    void passwordTokenRecovery(@Valid RecoveryTokenDTO dto);
    void changePassword(@Valid ChangePasswordDTO dto, String tokenValue);
}
