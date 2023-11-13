package com.viethung.service.client;

import com.viethung.dto.ProductCardDto;
import com.viethung.entity.Category;
import com.viethung.entity.Product;
import com.viethung.repository.CategoryRepository;
import com.viethung.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductAllServiceImpl {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private HomeServiceImpl homeService;

    public Page<ProductCardDto> findAll(List<String> category,
                                        BigDecimal priceMin,
                                        BigDecimal priceMax,
                                        Pageable pageable) {
        Page<Product> products;
        if (category != null) {
            // tìm các thằng khác
            List<Category> categories = categoryRepository.findAllByCodeIn(category);
            products = productRepository.findAllByCategoryInAndPriceBetween(categories,priceMin,priceMax,pageable);
            System.out.println("Category not null");
        } else {
            products = productRepository.findAllByPriceBetween(priceMin, priceMax, pageable);
            System.out.println("Category null");
        }

        List<ProductCardDto> productCardDtos = new ArrayList<>();
        products.forEach(product -> {
            productCardDtos.add(homeService.mapProductToProductCardDto(product));
        });
        return new PageImpl<>(productCardDtos, pageable, products.getTotalElements());
    }
}
