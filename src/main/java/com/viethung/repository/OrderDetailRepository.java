package com.viethung.repository;

import com.viethung.entity.Order;
import com.viethung.entity.OrderDetail;
import com.viethung.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
    boolean existsByCode(String code);
}
