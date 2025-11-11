package br.com.financial.manager.app.service;

import br.com.financial.manager.app.domain.entity.dto.CreateAccountDTO;
import br.com.financial.manager.app.domain.entity.dto.LoginResponse;
import br.com.financial.manager.app.domain.entity.dto.RegisterUserDTO;
import br.com.financial.manager.app.infrastructure.security.jwt.UsersLoginDTO;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UsersRegisterService {

    void registerUser(@Valid RegisterUserDTO dto);

    LoginResponse login(@Valid UsersLoginDTO dto);

    void createAccount(@Valid CreateAccountDTO dto);
}
