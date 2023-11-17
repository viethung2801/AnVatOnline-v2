package com.viethung.dto;

import com.viethung.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfitRevenueReport {
    private String label;
    private Integer profit;
    private Integer revenue;
}
