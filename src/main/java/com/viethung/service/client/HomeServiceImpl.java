package com.viethung.service.client;

import com.viethung.dto.ProductCardDto;
import com.viethung.entity.Category;
import com.viethung.entity.Product;
import com.viethung.repository.CategoryRepository;
import com.viethung.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomeServiceImpl {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<ProductCardDto> findTop8BestSeller() {
        Pageable pageable = PageRequest.of(0, 8);
        List<Product> products = productRepository.findTop8BestSeller(pageable);
        List<ProductCardDto> productCardDtos = new ArrayList<>();
        products.forEach(product -> {
            productCardDtos.add(mapProductToProductCardDto(product));
        });
        return productCardDtos;
    }

    public List<ProductCardDto> findTop8ProductNew() {
        Pageable pageable = PageRequest.of(0, 8);
        List<Product> products = productRepository.findTop8ProductNew(pageable);
        List<ProductCardDto> productCardDtos = new ArrayList<>();
        products.forEach(product -> {
            productCardDtos.add(mapProductToProductCardDto(product));
        });
        return productCardDtos;
    }

    public ProductCardDto mapProductToProductCardDto(Product product) {
        ProductCardDto productCardDto = ProductCardDto.builder().build();
        productCardDto.setId(product.getId());
        productCardDto.setName(product.getName());
        productCardDto.setPrice(product.getPrice());
        productCardDto.setImageName(product.getProductImages().get(0).getUrl());

        return productCardDto;
    }
}
