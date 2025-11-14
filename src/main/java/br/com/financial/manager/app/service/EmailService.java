package br.com.financial.manager.app.service;

public interface EmailService {

    void sendEmailToken(String email, String token);
    public void sendEmailStatement(byte[] pdfBytes, String email);
}
