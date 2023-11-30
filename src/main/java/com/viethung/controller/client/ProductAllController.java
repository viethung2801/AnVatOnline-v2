package com.viethung.controller.client;

import com.viethung.dto.ProductCardDto;
import com.viethung.service.ProductAllService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductAllController {
    @Autowired
    private ProductAllService productAllService;

    @GetMapping("/product-all")
    public String displayView(@RequestParam Optional<Integer> page,
                              @RequestParam Optional<List<String>> category,
                              @RequestParam Optional<BigDecimal> priceMin,
                              @RequestParam Optional<BigDecimal> priceMax,
                              Model model) {
        Pageable pageable = PageRequest.of(page.orElse(0), 30);
        Page<ProductCardDto> productCardDtos = productAllService.findAll(
                category.orElse(null),
                priceMin.orElse(BigDecimal.valueOf(Long.MIN_VALUE)),
                priceMax.orElse(BigDecimal.valueOf(Long.MAX_VALUE)),
                pageable);
        model.addAttribute("productCardDtos", productCardDtos);
        return "client/page/product-all";
    }

    @GetMapping("/product-all/search")
    public String onSearchByKeys(@RequestParam Optional<Integer> page,
                                 @RequestParam Optional<String> keys,
                                 Model model) {


        Pageable pageable = PageRequest.of(page.orElse(0), 30);
        Page<ProductCardDto> productCardDtos = productAllService.findAllByKeys(keys.orElse(""),pageable);
        model.addAttribute("productCardDtos", productCardDtos);
        return "client/page/product-all";
    }
}
