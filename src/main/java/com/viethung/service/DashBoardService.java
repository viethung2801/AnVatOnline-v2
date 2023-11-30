package com.viethung.service;

import com.viethung.dto.ProductReportDto;
import com.viethung.dto.ProfitRevenueReport;

import java.time.LocalDate;
import java.util.List;

public interface DashBoardService {
    int getRevenueToday();

    List<ProductReportDto> getProductReports(String bestSale);

    int getOrderToday();

    int getOrderSuccessToday();

    int getOrderCancelToday();

    int getOrderProcessToday();

    ProfitRevenueReport getProfitRevenueReportByDate(LocalDate date);

    List getData(Integer filter);

    List<ProfitRevenueReport> getProfitRevenueReports(Integer fillter);
}
