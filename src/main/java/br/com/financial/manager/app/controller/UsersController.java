package br.com.financial.manager.app.controller;

import br.com.financial.manager.app.domain.entity.dto.*;
import br.com.financial.manager.app.exception.GlobalExceptionResponse;
import br.com.financial.manager.app.infrastructure.security.jwt.UsersLoginDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Users", description = "Users operations")
public interface UsersController {

    @Operation(summary = "Users Register", description = "Create user with username and email")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Register successfully"),
            @ApiResponse(responseCode = "409", description = "Email invalid or already exists", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class), mediaType = "application/json"))
    })
    ResponseEntity<Void> registerUser(RegisterUserDTO dto);

    @Operation(summary = "Users Login", description = "Operation to log in user and return token JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(schema = @Schema(implementation = LoginResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Email or Password invalid", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class), mediaType = "application/json"))
    })
    ResponseEntity<LoginResponse> login(UsersLoginDTO dto);

    @Operation(summary = "Users are possible to create accounts")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created"),
            @ApiResponse(responseCode = "404", description = "Invalid Email", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class), mediaType = "application/json"))
    })
    @SecurityRequirement(name = "bearer-key")
    ResponseEntity<Void> createAccount(CreateAccountDTO dto);

    @Operation(summary = "Users can recover password is had forgotten")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Accepted, token sent"),
            @ApiResponse(responseCode = "404", description = "Invalid Email", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class), mediaType = "application/json"))
    })
    ResponseEntity<Void> recoveryToken(RecoveryTokenDTO dto);

    @Operation(summary = "With the token sent by email, users can change their password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid recovery token", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Token already used", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class), mediaType = "application/json")),
    })
    ResponseEntity<Void> changePassword(ChangePasswordDTO dto, String token);
}
