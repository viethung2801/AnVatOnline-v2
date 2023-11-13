package com.viethung.service.client;

import com.viethung.dto.ProductCardDto;
import com.viethung.dto.ProductDetailDto;
import com.viethung.entity.Product;
import com.viethung.repository.CategoryRepository;
import com.viethung.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class ProductDetailServiceImpl {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private HomeServiceImpl homeService;

    @Autowired
    private CategoryRepository categoryRepository;

    public ProductDetailDto findProductDetailById(UUID id) {
        Product product = productRepository.findById(id).orElse(null);
        ProductDetailDto productDetailDto;
        if (product == null) {
            return ProductDetailDto.builder().build();
        }
        productDetailDto = mapProductToProductDetailDto(product);
        return productDetailDto;
    }

    public ProductDetailDto mapProductToProductDetailDto(Product product) {
        ProductDetailDto productDetailDto = ProductDetailDto.builder().build();
        productDetailDto.setId(product.getId());
        productDetailDto.setName(product.getName());
        productDetailDto.setCategoryCode(product.getCategory().getCode());
        productDetailDto.setCategoryName(product.getCategory().getName());
        productDetailDto.setDescription(product.getDescription());
        productDetailDto.setPrice(product.getPrice());
        if (product.getProductImages() != null) {

            productDetailDto.setImageNames(product.getProductImages().stream().map(
                    productImage -> productImage.getUrl()).toList()
            );
        }
        return productDetailDto;
    }

    public List<ProductCardDto> findTop8Related() {

        List<Product> products = productRepository.findAll();
        List<ProductCardDto> productCardDtos = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            productCardDtos.add(homeService.mapProductToProductCardDto(products.get(new Random().nextInt(products.size()))));
        }
        return productCardDtos;
    }
}
