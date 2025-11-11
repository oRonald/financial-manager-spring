package br.com.financial.manager.app.service;

import br.com.financial.manager.app.domain.entity.dto.CreateAccountDTO;
import br.com.financial.manager.app.domain.entity.dto.RegisterUserDTO;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UsersService {

    void registerUser(@Valid RegisterUserDTO dto);

    void createAccount(@Valid CreateAccountDTO dto);
}
