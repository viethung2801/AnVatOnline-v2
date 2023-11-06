package com.viethung.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDto {
    private UUID id;

    @NotBlank(message = "Mã không được trống")
    private String code;

    private MultipartFile imageUrl;
    private String imageName;

    @NotBlank(message = "Tên không được trống")
    private String firstName;

    private String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateBirth;

    @NotNull(message = "Chưa chọn giới tính")
    private Boolean gender;

    @NotBlank(message = "Email không được trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Số điện thoại không đựợc trống")
    private String phoneNumber;


    private String address;

//    @NotBlank(message = "Mật khẩu không được trống")
    private String password;
}
