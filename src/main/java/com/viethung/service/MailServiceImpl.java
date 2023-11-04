package com.viethung.service;

import com.viethung.dto.MailModel;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Có thể gửi email vào hàng đợi để tránh lag server khi gửi quá nhiều mail
 * Dùng addMail để hoạt động
 */

@Service
public class MailServiceImpl {
    @Value("${spring.mail.username}")
    private String from;
    private List<MailModel> mailModels = new ArrayList<>();

    @Autowired
    private JavaMailSender javaMailSender;

    @Scheduled(fixedDelay = 3000)
    public void run() {
        while (!mailModels.isEmpty()) {
            MailModel model = mailModels.remove(0);
            try {
                sendEmail(model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addMail(MailModel mailModel) {
        mailModels.add(mailModel);
    }

    public String sendEmail(MailModel mailModel) throws MessagingException {
        try {
            //Khởi tạo đối tượng chứa thông tin
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            //Set các thuộc tính
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(mailModel.getTo());
            mimeMessageHelper.setSubject(mailModel.getSubject());
            mimeMessageHelper.setText(mailModel.getBody());
            if (mailModel.getCc() != null) {
                mimeMessageHelper.setCc(mailModel.getCc());
            }
            if (mailModel.getBcc() != null) {
                mimeMessageHelper.setBcc(mailModel.getBcc());
            }
            if (mailModel.getAttachments() != null){
                for (int i = 0; i < mailModel.getAttachments().length; i++) {
                    mimeMessageHelper.addAttachment(
                            mailModel.getAttachments()[i].getOriginalFilename(),
                            new ByteArrayResource(mailModel.getAttachments()[i].getBytes())
                    );
                }
            }

            javaMailSender.send(mimeMessage);
            return "send success";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
