package br.com.financial.manager.app.exception;

public class AccountNameAlreadyExists extends RuntimeException {

    public AccountNameAlreadyExists(String message) {
        super(message);
    }
}
