package com.viethung.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CheckoutDto {
    @NotBlank(message = "Tên người nhận không được trống")
    private String name;

    @NotBlank(message = "Số điện thoại không được trống")
    private String phoneNumber;

    private String email;

    @NotBlank(message = "Địa ch không được trống")
    private String address;

    private String note;

    private List<UUID> cartDetailIds;
}
