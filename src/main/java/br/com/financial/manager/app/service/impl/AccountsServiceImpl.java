package br.com.financial.manager.app.service.impl;

import br.com.financial.manager.app.domain.entity.Account;
import br.com.financial.manager.app.domain.entity.Category;
import br.com.financial.manager.app.domain.entity.Transaction;
import br.com.financial.manager.app.domain.entity.Users;
import br.com.financial.manager.app.domain.entity.dto.TransactionEntryDTO;
import br.com.financial.manager.app.domain.entity.enums.TransactionType;
import br.com.financial.manager.app.domain.entity.dto.TransactionResponse;
import br.com.financial.manager.app.exception.exceptions.*;
import br.com.financial.manager.app.infrastructure.repository.postgres.AccountRepository;
import br.com.financial.manager.app.infrastructure.repository.postgres.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    @Override
    public TransactionResponse makeTransaction(TransactionEntryDTO dto, String accountName) {
        Users user = getUser();
        Account account = repository.findByNameAndOwnerId(accountName, user.getId()).orElseThrow(() -> new AccountNotFoundException("Account not found"));

        TransactionType type = fromString(dto.getType());

        if(type == null){
            throw new TransactionTypeException("Invalid Transaction type value: " + dto.getType());
        }

        if(type != TransactionType.EXPENSE && type != TransactionType.INCOME){
            throw new TransactionTypeException("Transaction type must be: EXPENSE or INCOME");
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

        Category category;
        Transaction transaction;

        if(!dto.getCategoryName().isEmpty() && !categoryRepository.existsByName(dto.getCategoryName())){
            category = Category.builder()
                    .name(dto.getCategoryName())
                    .transactions(new ArrayList<>())
                    .build();

            transaction = Transaction.builder()
                    .type(TransactionType.valueOf(dto.getType().toUpperCase()))
                    .description(dto.getDescription())
                    .transactionValue(dto.getValue())
                    .date(Instant.now())
                    .account(account)
                    .category(category)
                    .build();

            category.getTransactions().add(transaction);
        }

        if(!dto.getCategoryName().isEmpty() && categoryRepository.existsByName(dto.getCategoryName())){
            Category categoryExists = categoryRepository.findByName(dto.getCategoryName());

            transaction = Transaction.builder()
                    .type(TransactionType.valueOf(dto.getType().toUpperCase()))
                    .description(dto.getDescription())
                    .transactionValue(dto.getValue())
                    .date(Instant.now())
                    .account(account)
                    .category(categoryExists)
                    .build();

            categoryExists.getTransactions().add(transaction);
        } else {
            transaction = Transaction.builder()
                    .type(TransactionType.valueOf(dto.getType().toUpperCase()))
                    .description(dto.getDescription())
                    .transactionValue(dto.getValue())
                    .date(Instant.now())
                    .account(account)
                    .category(null)
                    .build();
        }

        transactionRepository.save(transaction);
        return new TransactionResponse(account.getId(), dto.getDescription(), dto.getValue(), transaction.getType(), dto.getCategoryName(), Instant.now());
    }

    private Users getUser(){
        return usersRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private TransactionType fromString(String type){
        for(TransactionType t : TransactionType.values()){
            if(t.name().equals(type)){
                return t;
            }
        }
        return null;
    }
}
