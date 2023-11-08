package com.viethung.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListDto {
    private String id;
    private String imageUrl;
    private String code;
    private String name;
    private String categoryName;
    private int status;
    private String price;
    private LocalDateTime createdDate;
}
