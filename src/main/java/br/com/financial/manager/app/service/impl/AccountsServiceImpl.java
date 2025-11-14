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
import br.com.financial.manager.app.infrastructure.config.pdf.DocumentWrapper;
import br.com.financial.manager.app.infrastructure.config.pdf.PdfFactoryConfig;
import br.com.financial.manager.app.infrastructure.repository.mongodb.TransactionsAuditRepository;
import br.com.financial.manager.app.infrastructure.repository.postgres.AccountRepository;
import br.com.financial.manager.app.infrastructure.repository.postgres.CategoryRepository;
import br.com.financial.manager.app.infrastructure.repository.postgres.TransactionRepository;
import br.com.financial.manager.app.infrastructure.repository.postgres.UsersRepository;
import br.com.financial.manager.app.service.AccountsService;
import br.com.financial.manager.app.service.EmailService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AccountsServiceImpl implements AccountsService {

    private final AccountRepository repository;
    private final UsersRepository usersRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final PdfFactoryConfig pdfFactoryConfig;
    private final EmailService emailService;
    private final TransactionsAuditRepository auditRepository;

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

    @Override
    public List<TransactionResponse> getTransactionsByAccount(String accountName) {
        Users user = getUser();
        Account account = repository.findByNameAndOwnerId(accountName, user.getId()).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        List<Transaction> transactions = transactionRepository.findByAccountId(account.getId());

        if(transactions == null){
            throw new RuntimeException("Transactions not found");
        }

        return transactions.stream()
                .map(this::toResponse)
                .toList();
    }

    private TransactionResponse toResponse(Transaction transaction){
        return TransactionResponse.builder()
                .accountId(transaction.getAccount().getId())
                .description(transaction.getDescription())
                .transactionValue(transaction.getTransactionValue())
                .type(transaction.getType())
                .categoryName(transaction.getCategory() != null ? transaction.getCategory().getName() : "UNCATEGORIZED")
                .date(transaction.getDate())
                .build();
    }

    @Override
    public void sendStatementReport(String accountName) {
        Users user = getUser();
        Account account = repository.findByNameAndOwnerId(accountName, user.getId()).orElseThrow(() -> new AccountNotFoundException("Account not found"));

        try{
            DocumentWrapper wrapper = pdfFactoryConfig.createDocument();
            Document doc = wrapper.getDocument();

            Paragraph title = new Paragraph("Financial Statement Report");
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10f);
            doc.add(title);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.setWidths(new float[]{6f, 5f, 5f, 6f, 6f, 6f});

            Stream.of("Description", "Transaction Value", "Date", "Type", "Category",  "Account")
                    .forEach(header -> {
                        PdfPCell cell = new PdfPCell(new Paragraph(header));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        table.addCell(cell);
                    });

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                    .withZone(ZoneId.of("America/Sao_Paulo"));

            for(Transaction t : account.getTransactions()){
                String formatedDate = formatter.format(t.getDate());
                table.addCell(new PdfPCell(new Paragraph(t.getDescription().isEmpty() ? "UNKNOWN" : t.getDescription())));
                table.addCell(new PdfPCell(new Paragraph(String.valueOf(t.getTransactionValue()))));
                table.addCell(new PdfPCell(new Paragraph(formatedDate)));
                table.addCell(new PdfPCell(new Paragraph(t.getType().name())));
                table.addCell(new PdfPCell(new Paragraph(t.getCategory() != null ? t.getCategory().getName() : "UNKNOWN")));
                table.addCell(new PdfPCell(new Paragraph(t.getAccount().getName())));
            }

            doc.add(table);

            Paragraph info = new Paragraph("Account owner: " + user.getEmail());
            info.setAlignment(Element.ALIGN_LEFT);

            doc.add(info);

            byte[] pdfBytes = wrapper.getBytes();

            emailService.sendEmailStatement(pdfBytes, user.getEmail());
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
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
