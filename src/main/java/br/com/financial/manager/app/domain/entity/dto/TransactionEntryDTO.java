package br.com.financial.manager.app.domain.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransactionEntryDTO {

    private String description;

    @NotNull
    private BigDecimal value;

    @NotNull
    @NotBlank
    private String type;

    @NotNull
    private String categoryName;
}
