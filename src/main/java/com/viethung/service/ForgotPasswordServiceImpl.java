package com.viethung.service;

import com.viethung.dto.MailModel;
import com.viethung.entity.User;
import com.viethung.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ForgotPasswordServiceImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailServiceImpl mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void sendPassword(String email) throws MessagingException {
        String to = email;
        String subject = "Ăn Vặt Online! Mật Khẩu Mới";
        Integer password = new Random().nextInt(999999 - 100000) + 100000;
        String body = "Mật khẩu mới của bạn là: " + password;
        //Gửi email
        MailModel mailModel = MailModel.builder().build();
        mailModel.setTo(to);
        mailModel.setSubject(subject);
        mailModel.setBody(body);
        String message = mailService.sendEmail(mailModel);
        if (message != null) {
            System.out.println(password);
            //Thay đổi mật khẩu
            User user = userRepository.findUserByEmail(email).get();
            user.setPassword(passwordEncoder.encode(password.toString()));
            userRepository.save(user);
        }
    }
}
