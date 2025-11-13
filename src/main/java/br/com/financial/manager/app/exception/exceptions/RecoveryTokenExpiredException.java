package br.com.financial.manager.app.exception.exceptions;

public class RecoveryTokenExpiredException extends RuntimeException{

    public RecoveryTokenExpiredException(String message) {
        super(message);
    }
}
