package br.com.financial.manager.app.controller;

import br.com.financial.manager.app.domain.entity.dto.TransactionEntryDTO;
import br.com.financial.manager.app.domain.entity.dto.TransactionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;


public interface AccountsController {

    ResponseEntity<TransactionResponse> makeTransaction(TransactionEntryDTO dto, String accountName, UriComponentsBuilder builder);
}
