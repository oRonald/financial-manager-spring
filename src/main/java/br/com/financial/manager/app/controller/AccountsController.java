package br.com.financial.manager.app.controller;

import br.com.financial.manager.app.domain.entity.dto.TransactionEntryDTO;
import br.com.financial.manager.app.domain.entity.dto.TransactionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;


public interface AccountsController {

    ResponseEntity<TransactionResponse> makeTransaction(TransactionEntryDTO dto, String accountName, UriComponentsBuilder builder);
    ResponseEntity<List<TransactionResponse>> accountTransactions(String accountName);
}
