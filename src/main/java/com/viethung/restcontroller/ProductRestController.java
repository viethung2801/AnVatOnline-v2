package com.viethung.restcontroller;

import com.viethung.dto.ProductListDto;
import com.viethung.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductRestController {
    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/products/top7")
    public ResponseEntity<?> find7Product(@RequestParam(defaultValue = "") String keys) {
        List<ProductListDto> productListDtos = productService.searchTop7Product(keys);
        return ResponseEntity.ok(productListDtos);
    }
}
