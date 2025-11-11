package br.com.financial.manager.app.exception.exceptions;

public class TransactionValueException extends RuntimeException{

    public TransactionValueException(String message) {
        super(message);
    }
}
