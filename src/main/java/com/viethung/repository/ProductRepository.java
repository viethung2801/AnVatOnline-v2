package com.viethung.repository;

import com.viethung.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Product findByCode(String code);

    boolean existsByCode(String code);

    long countByCodeAndIdNot(String code, UUID id);

    Page<Product> searchAllByCodeLikeOrNameLike(String code, String name, Pageable pageable);
}
