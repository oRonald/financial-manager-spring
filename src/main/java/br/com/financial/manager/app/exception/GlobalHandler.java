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
    public ResponseEntity<InvalidEmailResponse> invalidEmail(InvalidEmailException ex){
        log.info("handling email invalid");
        InvalidEmailResponse response = InvalidEmailResponse.builder()
                .error("Register error")
                .message("Invalid email or email already exists")
                .status(HttpStatus.CONFLICT.value())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
