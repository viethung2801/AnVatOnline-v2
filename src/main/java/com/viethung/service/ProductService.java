package com.viethung.service;

import com.viethung.dto.ProductFormDto;
import com.viethung.dto.ProductListDto;
import com.viethung.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    Page<ProductListDto> findProducts(Pageable pageable);

    Page<ProductListDto> searchProducts(String keys, Pageable pageable);

    List<ProductListDto> searchTop7Product(String keys);

    ProductFormDto findProductFormById(UUID id);

    Product findProductDetailById(UUID id);

    boolean deleteById(UUID id);

    boolean handleAdd(ProductFormDto productFormDto);

    boolean handleUpdate(ProductFormDto productFormDto);

    boolean existsByCode(String code);

    long countByCodeAndIdNot(String code, UUID id);

    String generateCode();

    Product mapProductFormDtoToProduct(ProductFormDto productFormDto);

    ProductListDto mapProductToProductListDto(Product product);

    ProductFormDto mapProductToProductFormDto(Product product);
}
