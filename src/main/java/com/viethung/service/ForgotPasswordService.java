package com.viethung.service;

import jakarta.mail.MessagingException;

import java.util.UUID;

public interface ForgotPasswordService {
    boolean existsByEmail(String email);

    void sendPassword(String email) throws MessagingException;

    void confirmPassword(UUID id, String password);
}
