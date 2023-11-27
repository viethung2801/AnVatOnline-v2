package com.viethung.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class OrderDto {
    private String orderId;
    private String userId;
    private String userFullName;
    private String userPhoneNumber;
    private String orderCode;
    private LocalDateTime createdDate;
    private LocalDateTime confirmedDate;
    private LocalDateTime shippedDate;
    private LocalDateTime receivedDate;
    private int status;
    private int state;
    private String receiverName;
    private String address;
    private String email;
    private String phoneNumber;
    private String note;
    private int totalPrice;
    List<OrderDetailDto> orderDetailDtos;
}
