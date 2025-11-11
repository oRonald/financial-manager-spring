package br.com.financial.manager.app.controller;

import br.com.financial.manager.app.domain.entity.dto.CreateAccountDTO;
import br.com.financial.manager.app.domain.entity.dto.LoginResponse;
import br.com.financial.manager.app.domain.entity.dto.RegisterUserDTO;
import br.com.financial.manager.app.infrastructure.security.jwt.UsersLoginDTO;
import org.springframework.http.ResponseEntity;

public interface UsersController {

    ResponseEntity<Void> registerUser(RegisterUserDTO dto);
    ResponseEntity<LoginResponse> login(UsersLoginDTO dto);
    ResponseEntity<Void> createAccount(CreateAccountDTO dto);
}
