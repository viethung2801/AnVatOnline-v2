package com.viethung.service;

import com.viethung.dto.CategoryDto;
import com.viethung.entity.Category;
import com.viethung.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryServiceImpl {
    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public boolean existsByCode(String code) {
        return categoryRepository.existsByCode(code);
    }

    public CategoryDto findById(UUID id){
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null){
            return null;
        }
        CategoryDto categoryDto = CategoryDto.builder().
                id(category.getId())
                .code(category.getCode())
                .name(category.getName())
                .build();
        return categoryDto;
    }

    public String generateCategoryCode() {
        Long number = categoryRepository.count()+1;
        String code = "DM0" + number;
        while (true) {
            if (existsByCode(code)) {
                number = number + 1;
                code = "DM0" + number;
            }
            break;
        }
        return code;
    }

    public Category save(CategoryDto categoryDto){
        Category category = Category.builder()
                .id(categoryDto.getId())
                .code(categoryDto.getCode())
                .name(categoryDto.getName())
                .build();
        return categoryRepository.save(category);
    }

    public boolean deleteById(UUID id){
        try {
            categoryRepository.deleteById(id);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public Page<Category> search(String keys,Pageable pageable){
        keys = "%"+keys+"%";
        Page<Category> categories = categoryRepository.searchByKeys(keys,pageable);
        return categories;
    }
}
