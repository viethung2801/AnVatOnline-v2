package com.viethung.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailDto {
    private UUID id;
    private String name;
    private BigDecimal price;
    private String description;
    private String categoryName;
    private String categoryCode;
    private List<String> imageNames;
}
