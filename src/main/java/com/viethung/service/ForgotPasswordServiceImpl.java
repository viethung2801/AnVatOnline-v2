package com.viethung.service;

import com.viethung.dto.MailModel;
import com.viethung.entity.User;
import com.viethung.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

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
        String id = userRepository.findUserByEmail(email).get().getId().toString();
        String to = email;
        String subject = "Ăn Vặt Online! Mật Khẩu Mới";
        Integer password = new Random().nextInt(999999 - 100000) + 100000;
        String body = "        <p>Mật khẩu mới của bạn là:" + password + " </p><br>\n" +
                "        <a href=\"http://localhost:8080/forgot-password/"+id+"/"+password+"\">Bấm vào đây để thay đổi mật khẩu</a>";
        //Gửi email
        MailModel mailModel = MailModel.builder().build();
        mailModel.setTo(to);
        mailModel.setSubject(subject);
        mailModel.setBody(body);
        mailService.sendEmail(mailModel);

    }

    public void confirmPassword(UUID id, String password) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return;
        }
        user.setPassword(passwordEncoder.encode(password.toString()));
        userRepository.save(user);
    }
}
