package br.com.financial.manager.app.exception.exceptions;

public class InvalidRecoveryTokenException extends RuntimeException{

    public InvalidRecoveryTokenException(String message) {
        super(message);
    }
}
