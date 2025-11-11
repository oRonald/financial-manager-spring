package br.com.financial.manager.app.exception;

import com.auth0.jwt.exceptions.JWTCreationException;
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

    @ExceptionHandler(JWTCreationException.class)
    public ResponseEntity<GlobalExceptionResponse> handlingErrorGenerationToken(JWTCreationException e){
        GlobalExceptionResponse response = GlobalExceptionResponse
                .builder()
                .error("Token error")
                .message("Error generation token")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
