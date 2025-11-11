package br.com.financial.manager.app.service.impl;

import br.com.financial.manager.app.domain.entity.Users;
import br.com.financial.manager.app.domain.entity.dto.LoginResponse;
import br.com.financial.manager.app.infrastructure.security.jwt.JwtConfiguration;
import br.com.financial.manager.app.infrastructure.security.jwt.UsersLoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {

    private final AuthenticationManager manager;
    private final JwtConfiguration jwt;

    public LoginResponse login(UsersLoginDTO dto){
        var authToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        var authenticated = manager.authenticate(authToken);

        Users user = (Users) authenticated.getPrincipal();
        String token = jwt.generateToken(user);

        return new LoginResponse(token);
     }
}
