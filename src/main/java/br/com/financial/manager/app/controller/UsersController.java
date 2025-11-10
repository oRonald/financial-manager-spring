package br.com.financial.manager.app.controller;

import br.com.financial.manager.app.domain.entity.dto.RegisterUserDTO;
import org.springframework.http.ResponseEntity;

public interface UsersController {

    ResponseEntity<Void> registerUser(RegisterUserDTO dto);
}
