package br.com.financial.manager.app.controller;

import br.com.financial.manager.app.domain.entity.dto.*;
import br.com.financial.manager.app.infrastructure.security.jwt.UsersLoginDTO;
import org.springframework.http.ResponseEntity;

public interface UsersController {

    ResponseEntity<Void> registerUser(RegisterUserDTO dto);
    ResponseEntity<LoginResponse> login(UsersLoginDTO dto);
    ResponseEntity<Void> createAccount(CreateAccountDTO dto);
    ResponseEntity<Void> recoveryToken(RecoveryTokenDTO dto);
    ResponseEntity<Void> changePassword(ChangePasswordDTO dto, String token);
}
