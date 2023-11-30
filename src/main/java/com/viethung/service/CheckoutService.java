package com.viethung.service;

import com.viethung.dto.CartDetailDto;
import com.viethung.dto.CheckoutDto;
import com.viethung.entity.CartDetail;
import com.viethung.entity.Order;
import com.viethung.entity.OrderDetail;
import com.viethung.entity.User;

import java.util.UUID;

public interface CheckoutService {
    CartDetailDto newCartDetailDto(UUID productId, Integer quantity);

    String handleCheckout(CheckoutDto checkoutDto, UUID userId);

    void sendOrderDetail(Order order);

    Order mapCheckoutDtoToOrder(CheckoutDto checkoutDto, User user);

    OrderDetail mapCartDetailToOrderDetail(Order order, CartDetail cartDetail);
}
