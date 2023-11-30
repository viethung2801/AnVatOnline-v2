package com.viethung.service.impl;

import com.viethung.dto.OrderDetailDto;
import com.viethung.entity.Order;
import com.viethung.entity.OrderDetail;
import com.viethung.entity.Product;
import com.viethung.repository.OrderDetailRepository;
import com.viethung.repository.OrderRepository;
import com.viethung.repository.ProductRepository;
import com.viethung.service.OrderDetailService;
import com.viethung.service.OrderService;
import com.viethung.utilities.OrderUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderUtilities orderUtilities;

    @Autowired
    private OrderService orderService;

    @Override
    @Transactional
    public OrderDetailDto saveOrderDetail(OrderDetailDto orderDetailDto) {
        try {
            OrderDetail orderDetail = newOrderDetail(orderDetailDto);
            orderDetail = orderDetailRepository.save(orderDetail);
            System.out.println(orderDetail);
            return orderService.mapOrderDetailToOrderDetailDto(orderDetail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional
    public OrderDetailDto updateOrderDetail(OrderDetailDto orderDetailDto) {
        try {
            BigDecimal priceSale = BigDecimal.valueOf(Double.parseDouble(orderDetailDto.getPriceSale() + ".00"));
            orderDetailRepository.updateQuantityAndPriceSale(
                    orderDetailDto.getQuantity(),
                    priceSale,
                    UUID.fromString(orderDetailDto.getId()));
            return orderDetailDto;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public OrderDetail newOrderDetail(OrderDetailDto orderDetailDto) {
        OrderDetail orderDetail = OrderDetail.builder().build();
        Product product = productRepository.findProductById(UUID.fromString(orderDetailDto.getProductID()));
        //set value
        orderDetail.setCode(orderUtilities.generateOrderDetailCode());
        orderDetail.setOrder(Order.builder().id(UUID.fromString(orderDetailDto.getOrderID())).build());
        orderDetail.setProduct(Product.builder().id(UUID.fromString(orderDetailDto.getProductID())).build());
        orderDetail.setQuantity(orderDetailDto.getQuantity());
        orderDetail.setPrice(product.getPrice());
        orderDetail.setPriceSale(BigDecimal.valueOf(orderDetailDto.getPriceSale()));
        orderDetail.setCreatedDate(LocalDateTime.now());

        return orderDetail;
    }

    @Override
    @Transactional
    public OrderDetailDto deleteOrderDetail(UUID orderDetailId) {
        if (orderDetailId == null) {
            return null;
        }
        try {
            orderDetailRepository.deleteById(orderDetailId);
            return OrderDetailDto.builder().id(orderDetailId.toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
