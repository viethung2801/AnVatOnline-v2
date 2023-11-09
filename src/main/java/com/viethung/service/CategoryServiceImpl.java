package com.viethung.service;

import com.viethung.dto.CategoryDto;
import com.viethung.entity.Category;
import com.viethung.repository.CategoryRepository;
import com.viethung.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.rmi.server.ExportException;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UploadFileServiceImpl uploadFileService;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public boolean existsByCode(String code) {
        return categoryRepository.existsByCode(code);
    }

    public boolean existsByCodeAndIdNot(String code, UUID id) {
        return categoryRepository.existsByCodeAndIdNot(code, id);
    }

    public CategoryDto findById(UUID id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return null;
        }
        CategoryDto categoryDto = CategoryDto.builder().
                id(category.getId())
                .code(category.getCode())
                .name(category.getName())
                .imageName(category.getImageUrl())
                .build();
        return categoryDto;
    }

    public String generateCategoryCode() {
        Long number = categoryRepository.count() + 1;
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

    public boolean handleSave(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId()).get();
        try {
            category.setCode(categoryDto.getCode());
            category.setName(categoryDto.getName());
            if (!categoryDto.getImageFile().isEmpty()) {
                category.setImageUrl(uploadFileService.handleUpload(categoryDto.getImageFile()));
            }

            categoryRepository.save(category);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean handleAdd(CategoryDto categoryDto) {
        Category category = Category.builder().build();
        try {
            category.setCode(categoryDto.getCode());
            category.setName(categoryDto.getName());
            category.setImageUrl(uploadFileService.handleUpload(categoryDto.getImageFile()));

            categoryRepository.save(category);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteById(UUID id) {
        try {
            Category category = categoryRepository.findById(id).get();
            category.getProducts().forEach(product -> {
                product.setCategory(null);
            });
            categoryRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Page<Category> search(String keys, Pageable pageable) {
        keys = "%" + keys + "%";
        Page<Category> categories = categoryRepository.searchByKeys(keys, pageable);
        return categories;
    }
}
