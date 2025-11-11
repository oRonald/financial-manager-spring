package br.com.financial.manager.app.exception;

public class TokenGenerationErrorException extends RuntimeException{

    public TokenGenerationErrorException(String message) {
        super(message);
    }
}
