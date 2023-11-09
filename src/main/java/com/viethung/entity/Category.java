package com.viethung.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Table(name = "categories")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id ;

    @Column(name = "code", columnDefinition = "nvarchar(20)", unique = true, nullable = false)
    private String code;

    @Column(name = "name", nullable = false , columnDefinition = "nvarchar(50)")
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "category",cascade = CascadeType.DETACH)
    private List<Product> products;
}
