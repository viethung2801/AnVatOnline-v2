package com.viethung.service;

import com.viethung.dto.MailModel;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Scheduled;

public interface MailService {
    @Scheduled(fixedDelay = 3000)
    void run();

    void addMail(MailModel mailModel);

    String sendEmail(MailModel mailModel) throws MessagingException;
}
