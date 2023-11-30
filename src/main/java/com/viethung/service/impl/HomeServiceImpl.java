package com.viethung.service.impl;

import com.viethung.dto.ProductCardDto;
import com.viethung.entity.Category;
import com.viethung.entity.Product;
import com.viethung.repository.CategoryRepository;
import com.viethung.repository.ProductRepository;
import com.viethung.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<ProductCardDto> findTop12BestSeller() {
        LocalDate date = LocalDate.now();
        String[][] data = productRepository.findIdProductTop15BestSalesCreatedDateBetween(date.minusDays(30),date);

        List<UUID> productIds = Arrays.stream(data).map(strings -> UUID.fromString(strings[0])).toList();

        List<Product> products = productRepository.findAllById(productIds);

        List<ProductCardDto> productCardDtos = new ArrayList<>();

        products.forEach(product -> {
            productCardDtos.add(mapProductToProductCardDto(product));
        });
        return productCardDtos;
    }

    @Override
    public List<ProductCardDto> findTop8ProductNew() {
        Pageable pageable = PageRequest.of(0, 8);
        List<Product> products = productRepository.findTop8ProductNew(pageable);
        List<ProductCardDto> productCardDtos = new ArrayList<>();
        products.forEach(product -> {
            productCardDtos.add(mapProductToProductCardDto(product));
        });
        return productCardDtos;
    }

    @Override
    public ProductCardDto mapProductToProductCardDto(Product product) {
        ProductCardDto productCardDto = ProductCardDto.builder().build();
        productCardDto.setId(product.getId());
        productCardDto.setName(product.getName());
        productCardDto.setPrice(product.getPrice());
        productCardDto.setImageName(product.getProductImages().get(0).getUrl());

        return productCardDto;
    }
}
