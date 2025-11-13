package br.com.financial.manager.app.controller.impl;

import br.com.financial.manager.app.controller.UsersController;
import br.com.financial.manager.app.domain.entity.dto.*;
import br.com.financial.manager.app.infrastructure.security.jwt.UsersLoginDTO;
import br.com.financial.manager.app.service.UsersService;
import br.com.financial.manager.app.service.impl.AuthServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserControllerImpl implements UsersController {

    private final UsersService service;
    private final AuthServiceImpl authService;

    public UserControllerImpl(UsersService service, AuthServiceImpl authService) {
        this.service = service;
        this.authService = authService;
    }

    @Override
    @PostMapping
    public ResponseEntity<Void> registerUser(@RequestBody RegisterUserDTO dto) {
        service.registerUser(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    @PostMapping("/recovery-token")
    public ResponseEntity<Void> recoveryToken(@RequestBody RecoveryTokenDTO dto) {
        authService.passwordTokenRecovery(dto);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDTO dto, @RequestParam String token) {
        authService.changePassword(dto, token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UsersLoginDTO dto) {
        LoginResponse response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/account")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> createAccount(@RequestBody CreateAccountDTO dto) {
        service.createAccount(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
