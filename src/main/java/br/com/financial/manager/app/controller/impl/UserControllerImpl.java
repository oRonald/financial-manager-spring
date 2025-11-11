package br.com.financial.manager.app.controller.impl;

import br.com.financial.manager.app.controller.UsersController;
import br.com.financial.manager.app.domain.entity.dto.LoginResponse;
import br.com.financial.manager.app.domain.entity.dto.RegisterUserDTO;
import br.com.financial.manager.app.infrastructure.security.jwt.UsersLoginDTO;
import br.com.financial.manager.app.service.UsersRegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserControllerImpl implements UsersController {

    private final UsersRegisterService service;

    public UserControllerImpl(UsersRegisterService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    public ResponseEntity<Void> registerUser(@RequestBody RegisterUserDTO dto) {
        service.registerUser(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UsersLoginDTO dto) {
        LoginResponse response = service.login(dto);
        return ResponseEntity.ok(response);
    }

}
