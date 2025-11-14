package br.com.financial.manager.app.controller.impl;

import br.com.financial.manager.app.controller.AccountsController;
import br.com.financial.manager.app.domain.entity.dto.TransactionEntryDTO;
import br.com.financial.manager.app.domain.entity.dto.TransactionResponse;
import br.com.financial.manager.app.service.AccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountsControllerImpl implements AccountsController {

    private final AccountsService service;

    @Override
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TransactionResponse> makeTransaction(@RequestBody TransactionEntryDTO dto, @RequestParam("accountName") String accountName, UriComponentsBuilder builder) {
        TransactionResponse response = service.makeTransaction(dto, accountName);
        URI uri = builder.path("/{id}").buildAndExpand(response.getAccountId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @Override
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TransactionResponse>> accountTransactions(@RequestParam String accountName) {
        List<TransactionResponse> responses = service.getTransactionsByAccount(accountName);
        return ResponseEntity.ok().body(responses);
    }

    @Override
    @PostMapping("/statement-report")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> statementReport(@RequestParam String accountName) {
        service.sendStatementReport(accountName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
