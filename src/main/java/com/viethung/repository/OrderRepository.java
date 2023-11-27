package com.viethung.repository;

import com.viethung.entity.ENUM.EOrderState;
import com.viethung.entity.ENUM.EOrderStatus;
import com.viethung.entity.Order;
import com.viethung.entity.Product;
import com.viethung.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    boolean existsByCode(String code);

    @Query("select o from Order o join OrderDetail od on o.id = od.order.id where od.product.id = :id order by o.createdDate desc ")
    Page<Order> findAllOrderByProductId(UUID id, Pageable pageable);

    @Query("select o from Order o where o.user.id = :id order by o.createdDate desc ")
    Page<Order> findAllOrderByUser(UUID id, Pageable pageable);

    @Query("select o from Order o where (o.state in :state and o.status in :status) and (o.code = :code or o.phoneNumber like :phoneNumber or o.email like :email or o.receiverName like :receiverName)")
    Page<Order> searchByKeysAndStateAndStatus(String code, String phoneNumber, String email, String receiverName, List<EOrderState> state, List<EOrderStatus> status, Pageable pageable);

    @Query("select o from Order o where (o.state in :state and o.status in :status) and (o.code = :code or o.phoneNumber like :phoneNumber or o.email like :email or o.receiverName like :receiverName)and(cast(o.createdDate as localdate ) between :startDate and :endDate)")
    Page<Order> searchByKeysAndStateAndStatusAndDateBetween(String code, String phoneNumber, String email, String receiverName, List<EOrderState> state, List<EOrderStatus> status,LocalDate startDate,LocalDate endDate, Pageable pageable);

    @Query("select o,o.orderDetails from Order o where o.status = :status and o.state = :state and cast( o.createdDate as localdate ) = current date ")
    List<Order> getOrderSuccessToday(EOrderState state, EOrderStatus status);

    @Query("select o,o.orderDetails from Order o where o.state = :state and cast( o.createdDate as localdate ) = current date ")
    List<Order> getOrderSuccessToday(EOrderState state);

    @Query("select count(o) from Order o where cast(o.createdDate as localdate )  = current date ")
    Integer countOrderToday();

    @Query("select count(o) from Order o where o.state=:state and cast(o.createdDate as localdate )  = current date ")
    Integer countOrderTodayByState(EOrderState state);

    List<Order> findByState(EOrderState state);
}
