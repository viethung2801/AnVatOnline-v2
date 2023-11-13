package com.viethung.repository;

import com.viethung.entity.Category;
import com.viethung.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Product findByCode(String code);

    boolean existsByCode(String code);

    long countByCodeAndIdNot(String code, UUID id);

    Page<Product> searchAllByCodeLikeOrNameLike(String code, String name, Pageable pageable);
    @Query("select distinct p from Product p order by p.createdDate")
    List<Product> findTop8ProductNew(Pageable pageable);

    @Query("select distinct p from Product p order by p.createdDate")
    List<Product> findTop8BestSeller(Pageable pageable);

    List<Product> findAllByCategory(Category category);

    Page<Product> findAllByCategoryInAndPriceBetween(Collection<Category> category,BigDecimal price, BigDecimal price2, Pageable pageable);
    Page<Product> findAllByPriceBetween(BigDecimal price, BigDecimal price2, Pageable pageable);

    Product findProductById(UUID id);

    Page<Product> findAllByNameLike (String name, Pageable pageable);
}
