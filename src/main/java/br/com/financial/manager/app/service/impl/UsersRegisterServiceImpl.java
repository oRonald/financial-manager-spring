package br.com.financial.manager.app.service.impl;

import br.com.financial.manager.app.domain.entity.Role;
import br.com.financial.manager.app.domain.entity.Users;
import br.com.financial.manager.app.domain.entity.dto.LoginResponse;
import br.com.financial.manager.app.domain.entity.dto.RegisterUserDTO;
import br.com.financial.manager.app.exception.InvalidEmailException;
import br.com.financial.manager.app.infrastructure.repository.postgres.RoleRepository;
import br.com.financial.manager.app.infrastructure.repository.postgres.UsersRepository;
import br.com.financial.manager.app.infrastructure.security.jwt.JwtConfiguration;
import br.com.financial.manager.app.infrastructure.security.jwt.UsersLoginDTO;
import br.com.financial.manager.app.service.UsersRegisterService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UsersRegisterServiceImpl implements UsersRegisterService {

    private final UsersRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager manager;
    private final JwtConfiguration jwt;

    @Override
    @Transactional
    public void registerUser(RegisterUserDTO dto) {
        if(repository.existsByEmail(dto.getEmail()))
            throw new InvalidEmailException("Invalid email or email already exists");

        String hashPass = encoder.encode(dto.getPassword());
        Users user = Users.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(hashPass)
                .roles(new HashSet<>())
                .accounts(new ArrayList<>())
                .build();

        Role role = roleRepository.findByName("USER");
        role.getOwnerId().add(user);
        user.getRoles().add(role);

        repository.save(user);
    }

    @Override
    public LoginResponse login(UsersLoginDTO dto) {
        Users user = repository.findByEmail(dto.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(!encoder.matches(dto.getPassword(), user.getPassword())){
            throw new BadCredentialsException("Invalid password");
        }

        var authToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        var authenticated = manager.authenticate(authToken);
        String token = jwt.generateToken((Users) authenticated.getPrincipal());
        return new LoginResponse(token);
    }
}
