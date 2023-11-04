package com.viethung.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDto {
    @NotBlank(message = "Tên không được trống")
    private String firstName;

    private String lastName;

    @NotBlank(message = "Tên không được trống")
    @Email(message = "Email không hợp lệ")
    private String email;//username

    @NotBlank(message = "Tên không được trống")
    @Size(min = 10 ,max = 10,message = "Số điện thọai không hợp lệ")
    private String phoneNumber;

    @NotBlank(message = "Mật khẩu không được trống")
    private String password;
}
