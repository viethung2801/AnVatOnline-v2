package com.viethung.repository;

import com.viethung.entity.Order;
import com.viethung.entity.Product;
import com.viethung.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    boolean existsByCode(String code);

    @Query("select o from Order o join OrderDetail od on o.id = od.order.id where od.product.id = :id order by o.createdDate desc ")
    Page<Order> findAllOrderByProductId(UUID id, Pageable pageable);
    @Query("select o from Order o where o.user.id = :id order by o.createdDate desc ")
    Page<Order> findAllOrderByUser(UUID id, Pageable pageable);
}
