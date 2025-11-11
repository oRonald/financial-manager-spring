package br.com.financial.manager.app.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<GlobalExceptionResponse> invalidEmail(InvalidEmailException ex){
        log.info("handling email invalid");
        GlobalExceptionResponse response = GlobalExceptionResponse.builder()
                .error("Register error")
                .message("Invalid email or email already exists")
                .status(HttpStatus.CONFLICT.value())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(TokenGenerationErrorException.class)
    public ResponseEntity<GlobalExceptionResponse> handlingErrorGenerationToken(TokenGenerationErrorException e){
        GlobalExceptionResponse response = GlobalExceptionResponse
                .builder()
                .error("Token error")
                .message("Error generation token")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GlobalExceptionResponse> handlingUserNotFound(UserNotFoundException ex){
        GlobalExceptionResponse response = GlobalExceptionResponse
                .builder()
                .error("Token subject")
                .message("User not found")
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AccountNameAlreadyExists.class)
    public ResponseEntity<GlobalExceptionResponse> handlingAccountNameExists(AccountNameAlreadyExists ex){
        GlobalExceptionResponse response = GlobalExceptionResponse
                .builder()
                .error("Invalid account name")
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<GlobalExceptionResponse> handlingAccountNotFound(AccountNotFoundException ex){
        GlobalExceptionResponse response = GlobalExceptionResponse
                .builder()
                .error("Invalid account")
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<GlobalExceptionResponse> handlingInsufficientBalance(InsufficientBalanceException ex){
        GlobalExceptionResponse response = GlobalExceptionResponse
                .builder()
                .error("Insufficient balance")
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(TransactionValueException.class)
    public ResponseEntity<GlobalExceptionResponse> handlingInvalidTransactionValue(TransactionValueException ex){
        GlobalExceptionResponse response = GlobalExceptionResponse
                .builder()
                .error("Transaction value invalid")
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
