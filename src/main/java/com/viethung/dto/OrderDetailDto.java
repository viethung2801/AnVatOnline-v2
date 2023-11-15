package com.viethung.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDto {
    private String id;
    private float quantity;
    private int price;
    private String productID;
    private String productImage;
    private String productName;
}
