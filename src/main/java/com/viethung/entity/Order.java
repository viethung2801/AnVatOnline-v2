package com.viethung.entity;

import com.viethung.entity.ENUM.EOrderState;
import com.viethung.entity.ENUM.EOrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Mod10Check;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Table(name = "orders")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "code", columnDefinition = "nvarchar(20)", unique = true, nullable = false)
    private String code;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdDate;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime confirmDate;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime shippedDate;


    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime receivedDate;

    @Column(name = "satus")
    @Enumerated(EnumType.ORDINAL)// Đánh theo số thứ tự trong ENUM
    private EOrderStatus status;//Enum : chờ xác nhận,đã xác nhận, đang giao, đã giao

    @Column(name = "state")
    @Enumerated(EnumType.ORDINAL)
    private EOrderState state; // Enum: Thành công, Hủy, Đang xử lý

    @Column(name = "receiver_name", nullable = false, columnDefinition = "nvarchar(50)")
    private String receiverName;

    @Column(name = "address", nullable = false, columnDefinition = "nvarchar(max)")
    private String address;

    @Column(name = "phone_number", nullable = false, columnDefinition = "nvarchar(10)")
    private String phoneNumber;

    @Column(name = "email", nullable = false, columnDefinition = "nvarchar(max)")
    private String email;// Anonymous: sẽ nhận xem đơn hàng qua email này

    @Column
    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    public int getTotalPrice() {
        int price = 0;
        if (!orderDetails.isEmpty()) {
            for (OrderDetail orderDetail : orderDetails) {
                price += (int) (orderDetail.getQuantity() * orderDetail.getPrice().intValue());
            }
        }
        return price;
    }
}
