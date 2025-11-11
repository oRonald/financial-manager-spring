package br.com.financial.manager.app.domain.entity.dto;

import br.com.financial.manager.app.domain.entity.Category;
import br.com.financial.manager.app.domain.entity.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
public class TransactionResponse {

    private Long accountId;
    private String description;
    private BigDecimal transactionValue;
    private TransactionType type;
    private String categoryName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao Paulo")
    private Instant date;
}
