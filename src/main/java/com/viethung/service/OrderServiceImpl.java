package com.viethung.service;

import com.viethung.dto.OrderDetailDto;
import com.viethung.dto.OrderDto;
import com.viethung.entity.ENUM.EOrderState;
import com.viethung.entity.ENUM.EOrderStatus;
import com.viethung.entity.Order;
import com.viethung.entity.OrderDetail;
import com.viethung.entity.Product;
import com.viethung.repository.OrderDetailRepository;
import com.viethung.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class OrderServiceImpl {
    @Autowired
    private OrderRepository orderRepository;

    public Page<OrderDto> findAllOrderDto(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        List<OrderDto> orderDtos = new ArrayList<>();
        orders.forEach(order -> {
            orderDtos.add(mapOrderToOrderDto(order));
        });
        return new PageImpl<>(orderDtos, pageable, orders.getTotalElements());
    }

    public OrderDto findOrderById(UUID id) {
        Order order = orderRepository.findById(id).get();
        return mapOrderToOrderDto(order);
    }

    public Page<OrderDto> findAllOrderByProduct(Product product,Pageable pageable) {
        Page<Order> orders = orderRepository.findAllOrderByProductId(product.getId(),pageable);
        List<OrderDto> orderDtos = new ArrayList<>();
        orders.forEach(order -> orderDtos.add(mapOrderToOrderDto(order)));
        return new PageImpl<OrderDto>(orderDtos,pageable,orders.getTotalElements());
    }

    public boolean handleUpdateStatus(UUID orderId, int status) {
        try {
            Order order = orderRepository.findById(orderId).get();
            switch (status) {
                case 0 -> {order.setStatus(EOrderStatus.ORDERED);
                    order.setCreatedDate(LocalDateTime.now());
                }
                case 1 -> {order.setStatus(EOrderStatus.CONFIRMED);
                    order.setConfirmDate(LocalDateTime.now());
                }
                case 2 -> {order.setStatus(EOrderStatus.SHIPPING);
                    order.setShippedDate(LocalDateTime.now());
                }
                case 3 -> {order.setStatus(EOrderStatus.RECEIVED);
                    order.setReceivedDate(LocalDateTime.now());
                    order.setState(EOrderState.SUCCESS);
                }
            }
            orderRepository.save(order);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public OrderDto mapOrderToOrderDto(Order order) {
        OrderDto orderDto = OrderDto.builder().build();

        orderDto.setOrderId(order.getId().toString());
        orderDto.setUserId(order.getUser() == null ? "" : order.getUser().getId().toString());
        orderDto.setUserFullName(order.getUser() == null ? "" : order.getUser().getLastName() + " " + order.getUser().getFirstName());
        orderDto.setOrderCode(order.getCode());
        orderDto.setCreatedDate(order.getCreatedDate());
        orderDto.setConfirmedDate(order.getConfirmDate());
        orderDto.setShippedDate(order.getShippedDate());
        orderDto.setReceivedDate(order.getReceivedDate());
        orderDto.setReceiverName(order.getReceiverName());
        orderDto.setAddress(order.getAddress());
        orderDto.setEmail(order.getEmail());
        orderDto.setPhoneNumber(order.getPhoneNumber());
        orderDto.setNote(order.getNote());
        orderDto.setTotalPrice(order.getTotalPrice());
        //
        List<OrderDetailDto> orderDetailDtos = new ArrayList<>();
        order.getOrderDetails().forEach(orderDetail -> orderDetailDtos.add(mapOrderDetailToOrderDetailDto(orderDetail)));
        orderDto.setOrderDetailDtos(orderDetailDtos);

        //
        switch (order.getStatus()) {
            case ORDERED -> {
                orderDto.setStatus(0);
            }
            case CONFIRMED -> {
                orderDto.setStatus(1);
            }
            case SHIPPING -> {
                orderDto.setStatus(2);
            }
            case RECEIVED -> {
                orderDto.setStatus(3);
            }
            default -> {
                orderDto.setStatus(4);
            }
        }
        switch (order.getState()) {
            case PROCESS -> {
                orderDto.setState(0);
            }
            case SUCCESS -> {
                orderDto.setState(1);
            }
            case CANCEL -> {
                orderDto.setState(2);
            }
            default -> {
                orderDto.setState(3);
            }
        }
        return orderDto;
    }


    public OrderDetailDto mapOrderDetailToOrderDetailDto(OrderDetail orderDetail) {
        OrderDetailDto orderDetailDto = OrderDetailDto.builder().build();

        orderDetailDto.setId(orderDetail.getId().toString());
        orderDetailDto.setQuantity(orderDetail.getQuantity());
        orderDetailDto.setPrice(orderDetail.getPrice().intValue());
        orderDetailDto.setProductID(orderDetail.getProduct().getId().toString());
        orderDetailDto.setProductName(orderDetail.getProduct().getName());
        orderDetailDto.setProductImage(orderDetail.getProduct().getProductImages().get(0).getUrl());

        return orderDetailDto;
    }
}

