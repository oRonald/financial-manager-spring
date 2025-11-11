package br.com.financial.manager.app.service;

import br.com.financial.manager.app.domain.entity.dto.TransactionEntryDTO;
import br.com.financial.manager.app.domain.entity.dto.TransactionResponse;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface AccountsService {

    TransactionResponse makeTransaction(@Valid TransactionEntryDTO dto, String accountName);
}
