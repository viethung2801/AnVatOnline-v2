package com.viethung.service;

import com.viethung.dto.OrderDetailDto;
import com.viethung.entity.OrderDetail;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface OrderDetailService {
    @Transactional
    OrderDetailDto saveOrderDetail(OrderDetailDto orderDetailDto);

    @Transactional
    OrderDetailDto updateOrderDetail(OrderDetailDto orderDetailDto);

    OrderDetail newOrderDetail(OrderDetailDto orderDetailDto);

    @Transactional
    OrderDetailDto deleteOrderDetail(UUID orderDetailId);
}
