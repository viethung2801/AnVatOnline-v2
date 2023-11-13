package com.viethung.controller.client;

import com.viethung.dto.ProductCardDto;
import com.viethung.dto.ProductDetailDto;
import com.viethung.service.client.ProductDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Controller
public class DetailProductController {
    @Autowired
    private ProductDetailServiceImpl productDetailService;
    @GetMapping("/product-detail")
    public String temp(Model model){
        return "client/page/product-detail";
    }

    @GetMapping("/product-detail/{id}")
    public String displayProductDetail(Model model, @PathVariable UUID id){
        ProductDetailDto productDetailDto = productDetailService.findProductDetailById(id);
        List<ProductCardDto> productCardDtos = productDetailService.findTop8Related();
        model.addAttribute("productDetailDto",productDetailDto);
        model.addAttribute("productCardDtos",productCardDtos);
        return "client/page/product-detail";
    }
}
