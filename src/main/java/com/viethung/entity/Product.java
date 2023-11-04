package com.viethung.entity;

import com.viethung.entity.ENUM.EProductStatus;
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
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Table(name = "products")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id ;

    @Column(name = "code", columnDefinition = "nvarchar(20)", unique = true, nullable = false)
    private String code;

    @Column(name = "name", columnDefinition = "nvarchar(100)", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "nvarchar(max)")
    private String description;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private EProductStatus status; //Enum: Đang bán, dừng bán

    @Column(name = "price", columnDefinition = "decimal(20,2)", nullable = false)
    private BigDecimal price;

    @Column(name = "cost", columnDefinition = "decimal(20,2)", nullable = false)
    private BigDecimal cost;

    @Column
    private float weight;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<ProductImage> productImages;
}
