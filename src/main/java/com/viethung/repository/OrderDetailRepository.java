package com.viethung.repository;

import com.viethung.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
    boolean existsByCode(String code);

    //    select SUM(od.quantity * p.price) 'Doanh thu', SUM((od.quantity * p.price)-(od.quantity * p.cost)) 'Lợi nhuận' from order_details od
//    join products p on od.product_id = p.id
//    where od.order_id in (select id from orders o where o.state = 0 and cast(o.created_date as date) = '2023-11-17')
    @Query("select sum((od.quantity * od.priceSale)-(od.quantity * p.cost)),sum(od.quantity * od.priceSale)" +
            "from OrderDetail od " +
            "join Product p on od.product.id = p.id " +
            "where od.order.id in (select o.id from Order o where o.state = 0 and cast(o.createdDate as localdate ) = :date)")
//state = 0 =>success
    Double[][] findProfitRevenueReportByDate(LocalDate date);

//    @Query("select od,od.product,od.order from OrderDetail od where od.id = :id")
//    OrderDetail findOrderDetailAndOrderAndProductById(UUID id);

    @Modifying
    @Query("update OrderDetail od set od.priceSale = :priceSale,od.quantity = :quantity where od.id = :id")
    void updateQuantityAndPriceSale(float quantity, BigDecimal priceSale, UUID id);
}
