package com.viethung.service;

import com.viethung.dto.OrderDetailDto;
import com.viethung.dto.OrderDto;
import com.viethung.entity.ENUM.EOrderState;
import com.viethung.entity.ENUM.EOrderStatus;
import com.viethung.entity.Order;
import com.viethung.entity.OrderDetail;
import com.viethung.entity.Product;
import com.viethung.repository.OrderRepository;
import com.viethung.utilities.OrderUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    @Autowired
    void setOrderRepository(OrderRepository orderRepository);

    @Autowired
    void setOrderUtilities(OrderUtilities orderUtilities);

    @Transactional
    OrderDto newOrderDtoPos();

    Page<OrderDto> findAllOrderDto(Pageable pageable);

    List<OrderDto> getOrderProcess();

    boolean onCancelOrder(UUID orderId);

    OrderDto findOrderById(UUID id);

    Page<OrderDto> findAllOrderByProduct(Product product, Pageable pageable);

    boolean handleUpdateStatus(UUID orderId, int status);

    OrderDto mapOrderToOrderDto(Order order);

    OrderDetailDto mapOrderDetailToOrderDetailDto(OrderDetail orderDetail);

    Page<OrderDto> search(String keys, Integer status, Integer state, Pageable pageable);

    @Transactional
    boolean deleteOrder(UUID orderId);

    EOrderState geteOrderState(Integer state);

    List<EOrderState> geteOrderStates(Integer state);

    EOrderStatus geteOrderStatus(Integer status);

    List<EOrderStatus> geteOrderStatues(Integer status);

    @Transactional
    OrderDto updateUserInOrder(OrderDto orderDto);

    boolean payOrder(UUID uuid);
}
