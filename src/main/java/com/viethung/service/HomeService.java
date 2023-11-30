package com.viethung.service;

import com.viethung.dto.ProductCardDto;
import com.viethung.entity.Category;
import com.viethung.entity.Product;

import java.util.List;

public interface HomeService {
    List<Category> findAll();

    List<ProductCardDto> findTop12BestSeller();

    List<ProductCardDto> findTop8ProductNew();

    ProductCardDto mapProductToProductCardDto(Product product);
}
