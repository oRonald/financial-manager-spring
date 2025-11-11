package br.com.financial.manager.app.exception.exceptions;

public class TokenGenerationErrorException extends RuntimeException{

    public TokenGenerationErrorException(String message) {
        super(message);
    }
}
