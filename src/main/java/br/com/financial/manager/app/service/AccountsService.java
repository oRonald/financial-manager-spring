package br.com.financial.manager.app.service;

import br.com.financial.manager.app.domain.entity.dto.TransactionEntryDTO;
import br.com.financial.manager.app.domain.entity.dto.TransactionResponse;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Validated
public interface AccountsService {

    TransactionResponse makeTransaction(@Valid TransactionEntryDTO dto, String accountName);
    List<TransactionResponse> getTransactionsByAccount(String accountName);
    void sendStatementReport(String accountName);
}
