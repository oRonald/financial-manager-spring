package br.com.financial.manager.app.service.impl;

import br.com.financial.manager.app.domain.entity.Account;
import br.com.financial.manager.app.domain.entity.Category;
import br.com.financial.manager.app.domain.entity.Transaction;
import br.com.financial.manager.app.domain.entity.Users;
import br.com.financial.manager.app.domain.entity.dto.TransactionEntryDTO;
import br.com.financial.manager.app.domain.entity.enums.TransactionType;
import br.com.financial.manager.app.domain.entity.dto.TransactionResponse;
import br.com.financial.manager.app.exception.exceptions.AccountNotFoundException;
import br.com.financial.manager.app.exception.exceptions.InsufficientBalanceException;
import br.com.financial.manager.app.exception.exceptions.TransactionValueException;
import br.com.financial.manager.app.exception.exceptions.UserNotFoundException;
import br.com.financial.manager.app.infrastructure.repository.postgres.AccountRepository;
import br.com.financial.manager.app.infrastructure.repository.postgres.TransactionRepository;
import br.com.financial.manager.app.infrastructure.repository.postgres.UsersRepository;
import br.com.financial.manager.app.service.AccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AccountsServiceImpl implements AccountsService {

    private final AccountRepository repository;
    private final UsersRepository usersRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public TransactionResponse makeTransaction(TransactionEntryDTO dto, String accountName) {
        Users user = getUser();
        Account account = repository.findByNameAndOwnerId(accountName, user.getId()).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        Category category = null;

        if(!dto.getDescription().isBlank()){
            category = Category.builder()
                    .name(dto.getCategoryName())
                    .transactions(new ArrayList<>())
                    .build();
        }

        if(dto.getType().equalsIgnoreCase("EXPENSE") && account.getBalance().compareTo(dto.getValue()) < 0){
            throw new InsufficientBalanceException("Insufficient balance");
        }

        if(dto.getValue().compareTo(BigDecimal.ZERO) <= 0){
            throw new TransactionValueException("Value must not be equals or below zero");
        }

        if(dto.getType().equalsIgnoreCase("EXPENSE")){
            account.setBalance(account.getBalance().subtract(dto.getValue()));
        } else {
            account.setBalance(account.getBalance().add(dto.getValue()));
        }

        Transaction transaction = Transaction.builder()
                .type(TransactionType.valueOf(dto.getType().toUpperCase()))
                .description(dto.getDescription())
                .transactionValue(dto.getValue())
                .date(Instant.now())
                .account(account)
                .category(category)
                .build();

        if(category != null){
            category.getTransactions().add(transaction);
        }

        transactionRepository.save(transaction);
        return new TransactionResponse(account.getId(), dto.getDescription(), dto.getValue(), transaction.getType(), dto.getCategoryName(), Instant.now());
    }

    private Users getUser(){
        return usersRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
