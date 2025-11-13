package br.com.financial.manager.app.domain.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordDTO {

    @NotBlank
    @NotNull
    private String newPassword;
}
