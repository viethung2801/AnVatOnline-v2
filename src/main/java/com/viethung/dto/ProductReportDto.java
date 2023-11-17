package com.viethung.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReportDto {
    private String id ;
    private String url;
    private String name;
    private BigDecimal price;
    private Float quantity;
    private Float revenue;
}
