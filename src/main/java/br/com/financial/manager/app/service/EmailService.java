package br.com.financial.manager.app.service;

public interface EmailService {

    void sendEmailToken(String email, String token);
}
