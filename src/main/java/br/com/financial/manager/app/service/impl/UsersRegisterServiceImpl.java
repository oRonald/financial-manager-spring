package br.com.financial.manager.app.service.impl;

import br.com.financial.manager.app.domain.entity.Account;
import br.com.financial.manager.app.domain.entity.Role;
import br.com.financial.manager.app.domain.entity.Users;
import br.com.financial.manager.app.domain.entity.dto.CreateAccountDTO;
import br.com.financial.manager.app.domain.entity.dto.LoginResponse;
import br.com.financial.manager.app.domain.entity.dto.RegisterUserDTO;
import br.com.financial.manager.app.exception.AccountNameAlreadyExists;
import br.com.financial.manager.app.exception.InvalidEmailException;
import br.com.financial.manager.app.exception.UserNotFoundException;
import br.com.financial.manager.app.infrastructure.repository.postgres.AccountRepository;
import br.com.financial.manager.app.infrastructure.repository.postgres.RoleRepository;
import br.com.financial.manager.app.infrastructure.repository.postgres.UsersRepository;
import br.com.financial.manager.app.infrastructure.security.jwt.JwtConfiguration;
import br.com.financial.manager.app.infrastructure.security.jwt.UsersLoginDTO;
import br.com.financial.manager.app.service.UsersRegisterService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersRegisterServiceImpl implements UsersRegisterService {

    private final UsersRepository repository;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
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

    @Override
    @Transactional
    public void createAccount(CreateAccountDTO dto) {
        Users user = getUser(dto.getEmail());
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!email.equals(user.getEmail())){
            throw new InvalidEmailException("Invalid email");
        }

        Account account = Account.builder()
                .name(dto.getAccountName())
                .balance(BigDecimal.ZERO)
                .owner(user)
                .transactions(new ArrayList<>())
                .build();

        validateAccountName(dto.getAccountName(), user.getId());

        user.getAccounts().add(account);

        accountRepository.save(account);
        log.info("Account created");
    }

    private Users getUser(String email){
        return repository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private void validateAccountName(String accountName, Long userId){
        if(accountRepository.existsByNameAndOwnerId(accountName, userId)){
            throw new AccountNameAlreadyExists("Account name already exists");
        }
    }
}
