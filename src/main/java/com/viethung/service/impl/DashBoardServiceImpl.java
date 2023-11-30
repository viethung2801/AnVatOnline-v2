package com.viethung.service.impl;

import com.viethung.dto.ProductReportDto;
import com.viethung.dto.ProfitRevenueReport;
import com.viethung.entity.ENUM.EOrderState;
import com.viethung.entity.Order;
import com.viethung.entity.Product;
import com.viethung.repository.OrderDetailRepository;
import com.viethung.repository.OrderRepository;
import com.viethung.repository.ProductRepository;
import com.viethung.service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class DashBoardServiceImpl implements DashBoardService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public int getRevenueToday() {
        List<Order> orders = orderRepository.getOrderSuccessToday(EOrderState.SUCCESS);
        System.out.println(orders.size());
        int revenue;
        revenue = orders.stream().map(Order::getTotalPrice).reduce(0, Integer::sum);
        return revenue;
    }

    @Override
    public List<ProductReportDto> getProductReports(String bestSale) {
        // data[productId,quantity,revenue]
        String[][] data= null;
        switch (bestSale) {
            case "today" -> {
                data = productRepository.findIdProductTop15BestSalesToday();

            }
            case "week" -> {
                LocalDate date = LocalDate.now();
                data = productRepository.findIdProductTop15BestSalesCreatedDateBetween(date.minusDays(7),date);
            }
            case "month" -> {
                LocalDate date = LocalDate.now();
                data = productRepository.findIdProductTop15BestSalesCreatedDateBetween(date.minusDays(30),date);
            }
        }
        //productIds
        List<UUID> productIds = Arrays.stream(data).map(strings -> UUID.fromString(strings[0])).toList();
        //products
        List<Product> products = productRepository.findAllById(productIds);

        List<ProductReportDto> productReportDtos = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            productReportDtos.add(ProductReportDto
                    .builder()
                    .id(data[i][0])
                    .url(products.get(i).getProductImages().get(0).getUrl())
                    .name(products.get(i).getName())
                    .price(products.get(i).getPrice())
                    .quantity(Float.parseFloat(data[i][1]))
                    .revenue(Float.parseFloat(data[i][2]))
                    .build());
        }
        System.out.println(productReportDtos);
        return productReportDtos;
    }

    @Override
    public int getOrderToday() {
        return orderRepository.countOrderToday();
    }

    @Override
    public int getOrderSuccessToday() {
        return orderRepository.countOrderTodayByState(EOrderState.SUCCESS);
    }

    @Override
    public int getOrderCancelToday() {
        return orderRepository.countOrderTodayByState(EOrderState.CANCEL);
    }

    @Override
    public int getOrderProcessToday() {
        return orderRepository.countOrderTodayByState(EOrderState.PROCESS);
    }

    @Override
    public ProfitRevenueReport getProfitRevenueReportByDate(LocalDate date) {
        Double[][] doubles = orderDetailRepository.findProfitRevenueReportByDate(date);
        return ProfitRevenueReport.builder()
                .label(date.getDayOfMonth() + "-" + date.getMonthValue())
                .profit(doubles[0][0] == null ? 0 : doubles[0][0].intValue())
                .revenue(doubles[0][1] == null ? 0 : doubles[0][1].intValue())
                .build();
    }

    @Override
    public List getData(Integer filter) {
        List data = new ArrayList<>();
        List<ProfitRevenueReport> reports = getProfitRevenueReports(filter);
        data.add(reports.stream().map(report -> report.getLabel()).toList());
        data.add(reports.stream().map(report -> report.getProfit()).toList());
        data.add(reports.stream().map(report -> report.getRevenue()).toList());
        return data;
    }

    @Override
    public List<ProfitRevenueReport> getProfitRevenueReports(Integer fillter) {
        List<ProfitRevenueReport> profitRevenueReports = new ArrayList<>();
        LocalDate date = LocalDate.now();
        for (int i = 0; i < fillter; i++) {
            profitRevenueReports.add(getProfitRevenueReportByDate(date.minusDays(i)));
        }
        return profitRevenueReports;
    }
}
