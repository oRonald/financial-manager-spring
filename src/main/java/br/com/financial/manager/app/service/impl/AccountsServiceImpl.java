package br.com.financial.manager.app.service.impl;

import br.com.financial.manager.app.domain.entity.Account;
import br.com.financial.manager.app.domain.entity.Category;
import br.com.financial.manager.app.domain.entity.Transaction;
import br.com.financial.manager.app.domain.entity.Users;
import br.com.financial.manager.app.domain.entity.audit.TransactionsAudit;
import br.com.financial.manager.app.domain.entity.dto.TransactionEntryDTO;
import br.com.financial.manager.app.domain.entity.enums.TransactionType;
import br.com.financial.manager.app.domain.entity.dto.TransactionResponse;
import br.com.financial.manager.app.exception.exceptions.*;
import br.com.financial.manager.app.infrastructure.config.mail.pdfGenerator.GenerateStatementReport;
import br.com.financial.manager.app.infrastructure.repository.mongodb.TransactionsAuditRepository;
import br.com.financial.manager.app.infrastructure.repository.postgres.AccountRepository;
import br.com.financial.manager.app.infrastructure.repository.postgres.CategoryRepository;
import br.com.financial.manager.app.infrastructure.repository.postgres.TransactionRepository;
import br.com.financial.manager.app.infrastructure.repository.postgres.UsersRepository;
import br.com.financial.manager.app.service.AccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountsServiceImpl implements AccountsService {

    private final AccountRepository repository;
    private final UsersRepository usersRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final GenerateStatementReport generateStatementReport;
    private final TransactionsAuditRepository auditRepository;

    @Override
    public TransactionResponse makeTransaction(TransactionEntryDTO dto, String accountName) {
        Users user = getUser();
        Account account = findAccountByUser(accountName, user);
        TransactionType transactionType = fromString(dto.getType());

        isBalanceSufficient(dto, account.getBalance());
        isTransactionEntryValueValid(dto.getValue());

        if(transactionType == TransactionType.EXPENSE){
            account.setBalance(account.getBalance().subtract(dto.getValue()));
        } else if(transactionType == TransactionType.INCOME) {
            account.setBalance(account.getBalance().add(dto.getValue()));
        }

        Category category = null;
        boolean hasCategory = dto.getCategoryName() != null && !dto.getCategoryName().isEmpty();

        if(hasCategory){
            category = categoryRepository.findByName(dto.getCategoryName());

            if(category == null){
                category = Category.builder()
                        .name(dto.getCategoryName())
                        .transactions(new ArrayList<>())
                        .build();
            }
        }

        Transaction transaction = Transaction.builder()
                .type(transactionType)
                .description(dto.getDescription())
                .transactionValue(dto.getValue())
                .account(account)
                .category(category)
                .date(Instant.now())
                .build();

        if(category != null){
            category.getTransactions().add(transaction);
        }

        transaction = transactionRepository.save(transaction);
        saveTransactionAudit(transaction, user.getId());

        return new TransactionResponse(
                account.getId(),
                dto.getDescription(),
                dto.getValue(),
                transaction.getType(),
                dto.getCategoryName(),
                Instant.now());
    }

    @Override
    public List<TransactionResponse> getTransactionsByAccount(String accountName) {
        Users user = getUser();
        Account account = findAccountByUser(accountName, user);
        List<Transaction> transactions = transactionRepository.findByAccountId(account.getId());

        if (transactions == null) {
            throw new IllegalArgumentException("Transactions not found");
        }

        return transactions.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void sendStatementReport(String accountName) {
        Users user = getUser();
        Account account = findAccountByUser(accountName, user);

        generateStatementReport.generateStatement(account);
    }

    private void saveTransactionAudit(Transaction transaction, Long userId){
        TransactionsAudit audit = new TransactionsAudit();

        audit.setId(transaction.getId());
        audit.setUserId(userId);
        audit.setTransactionTime(transaction.getDate());
        audit.setPeriodStart(LocalDate.now());

        auditRepository.save(audit);
    }

    private void isTransactionTypeNull(TransactionType type){
        if(type == null){
            throw new TransactionTypeException("Invalid Transaction type value");
        }
    }

    private void isTransactionTypeValid(TransactionType type){
        if(type != TransactionType.EXPENSE && type != TransactionType.INCOME){
            throw new TransactionTypeException("Transaction type must be EXPENSE or INCOME");
        }
    }

    private void isBalanceSufficient(TransactionEntryDTO dto, BigDecimal accountBalance){
        if(dto.getType().equalsIgnoreCase("EXPENSE") && accountBalance.compareTo(dto.getValue()) < 0){
            throw new InsufficientBalanceException("Insufficient balance");
        }
    }

    private void isTransactionEntryValueValid(BigDecimal transactionEntry){
        if(transactionEntry.compareTo(BigDecimal.ZERO) <= 0){
            throw new TransactionValueException("Value must not be equals or below zero");
        }
    }

    private Account findAccountByUser(String accountName, Users user){
        return  repository.findByNameAndOwnerId(accountName, user.getId()).orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }

    private Users getUser(){
        return usersRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private TransactionType fromString(String type){
        isTransactionTypeNull(TransactionType.valueOf(type));
        isTransactionTypeValid(TransactionType.valueOf(type));

        for(TransactionType t : TransactionType.values()){
            if(t.name().equals(type)){
                return t;
            }
        }
        return null;
    }

    private TransactionResponse toResponse(Transaction transaction){
        return TransactionResponse.builder()
                .accountId(transaction.getAccount().getId())
                .description(transaction.getDescription())
                .transactionValue(transaction.getTransactionValue())
                .type(transaction.getType())
                .categoryName(transaction.getCategory() != null ? transaction.getCategory().getName() : "No Category")
                .date(transaction.getDate())
                .build();
    }
}
