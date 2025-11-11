package br.com.financial.manager.app.exception;

public class TransactionValueException extends RuntimeException{

    public TransactionValueException(String message) {
        super(message);
    }
}
