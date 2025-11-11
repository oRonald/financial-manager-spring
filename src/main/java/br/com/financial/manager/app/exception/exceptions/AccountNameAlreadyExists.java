package br.com.financial.manager.app.exception.exceptions;

public class AccountNameAlreadyExists extends RuntimeException {

    public AccountNameAlreadyExists(String message) {
        super(message);
    }
}
