package br.com.financial.manager.app.service.impl;

import br.com.financial.manager.app.domain.entity.Users;
import br.com.financial.manager.app.domain.entity.dto.ChangePasswordDTO;
import br.com.financial.manager.app.domain.entity.dto.RecoveryTokenDTO;
import br.com.financial.manager.app.domain.entity.dto.LoginResponse;
import br.com.financial.manager.app.domain.entity.token.TokenRecovery;
import br.com.financial.manager.app.exception.exceptions.*;
import br.com.financial.manager.app.infrastructure.repository.mongodb.TokenRecoveryRepository;
import br.com.financial.manager.app.infrastructure.repository.postgres.UsersRepository;
import br.com.financial.manager.app.infrastructure.security.jwt.JwtConfiguration;
import br.com.financial.manager.app.infrastructure.security.jwt.UsersLoginDTO;
import br.com.financial.manager.app.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl {

    private final EmailService emailService;
    private final AuthenticationManager manager;
    private final PasswordEncoder encoder;
    private final JwtConfiguration jwt;
    private final UsersRepository repository;
    private final TokenRecoveryRepository tokenRepository;

    public LoginResponse login(UsersLoginDTO dto){
        var authToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        var authenticated = manager.authenticate(authToken);

        Users user = (Users) authenticated.getPrincipal();
        String token = jwt.generateToken(user);

        return new LoginResponse(token);
     }

     public void passwordTokenRecovery(@Valid RecoveryTokenDTO dto){
        Users user = findUserByEmail(dto.getEmail());
        tokenRepository.deleteAllByUserEmailAndIsUsedFalse(user.getEmail());
        String tokenValue = UUID.randomUUID().toString();

        TokenRecovery recovery = new TokenRecovery();
        recovery.setToken(tokenValue);
        recovery.setUserEmail(user.getEmail());
        recovery.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        recovery.setUsed(false);

        tokenRepository.save(recovery);

        emailService.sendEmailToken(user.getEmail(), tokenValue);
     }

     public void changePassword(@Valid ChangePasswordDTO dto, String tokenValue){
        TokenRecovery token = tokenRepository.findByToken(tokenValue).orElseThrow(() -> new InvalidRecoveryTokenException("Token does not exists"));

        if(token.isUsed()){
            throw new RecoveryTokenExpiredException("Token already used");
        }

        Users user = findUserByEmail(token.getUserEmail());
        user.setPassword(encoder.encode(dto.getNewPassword()));
        token.setUsed(true);

        repository.save(user);
        tokenRepository.save(token);
     }

     private Users findUserByEmail(String email){
        return repository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
     }
}
