package com.viethung.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailModel {

    private String from;//người gửi
    private String to;// người nhận
    private String[] cc;// Danh sách người nhận có cho xem tất cả người nhận
    private String[] bcc;// Danh sách người nhận Không cho xem tất cả người nhận
    private String subject;//Tiêu đề
    private String body;// Nội dung
    private MultipartFile[] attachments;//File đính kièm


}
