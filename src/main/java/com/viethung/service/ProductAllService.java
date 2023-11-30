package com.viethung.service;

import com.viethung.dto.ProductCardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductAllService {
    Page<ProductCardDto> findAll(List<String> category,
                                 BigDecimal priceMin,
                                 BigDecimal priceMax,
                                 Pageable pageable);

    Page<ProductCardDto> findAllByKeys(String keys,
                                       Pageable pageable);
}
