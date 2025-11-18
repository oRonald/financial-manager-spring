package br.com.financial.manager.app.controller;

import br.com.financial.manager.app.domain.entity.dto.TransactionEntryDTO;
import br.com.financial.manager.app.domain.entity.dto.TransactionResponse;
import br.com.financial.manager.app.exception.GlobalExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Tag(name = "Accounts", description = "Accounts operations")
@SecurityRequirement(name = "bearer-key")
public interface AccountsController {

    @Operation(summary = "Add Transactions", description = "Users can add transactions, either EXPENSE or INCOME")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transactions created", content = @Content(schema = @Schema(implementation = TransactionResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Account balance not sufficient", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Transaction value invalid", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Transaction type null", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Transaction type invalid", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class), mediaType = "application/json"))
    })
    ResponseEntity<TransactionResponse> makeTransaction(TransactionEntryDTO dto, String accountName, UriComponentsBuilder builder);

    @Operation(summary = "List of account transactions", description = "Return list of all account transaction")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account all transactions", content = @Content(schema = @Schema(implementation = TransactionResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class), mediaType = "application/json"))
    })
    ResponseEntity<List<TransactionResponse>> accountTransactions(String accountName);

    @Operation(summary = "Statement Report PDF", description = "Generation of statement report by email (must be a valid email account)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statement Report generated and sent"),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class), mediaType = "application/json"))
    })
    ResponseEntity<Void> statementReport(String accountName);
}
