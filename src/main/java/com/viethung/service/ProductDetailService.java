package com.viethung.service;

import com.viethung.dto.ProductCardDto;
import com.viethung.dto.ProductDetailDto;
import com.viethung.entity.Product;

import java.util.List;
import java.util.UUID;

public interface ProductDetailService {
    ProductDetailDto findProductDetailById(UUID id);

    ProductDetailDto mapProductToProductDetailDto(Product product);

    List<ProductCardDto> findTop8Related();
}
