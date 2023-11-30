package com.viethung.restcontroller;

import com.viethung.entity.Category;
import com.viethung.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryApi {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategory() {
        List<Category> categories = categoryService.findAll();
        categories.forEach(category -> category.setProducts(null));
        return ResponseEntity.ok(categories);
    }
}
