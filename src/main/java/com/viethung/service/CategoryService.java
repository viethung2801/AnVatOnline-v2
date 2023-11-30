package com.viethung.service;

import com.viethung.dto.CategoryDto;
import com.viethung.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> findAll();

    Page<Category> findAll(Pageable pageable);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, UUID id);

    CategoryDto findById(UUID id);

    String generateCategoryCode();

    boolean handleSave(CategoryDto categoryDto);

    boolean handleAdd(CategoryDto categoryDto);

    boolean deleteById(UUID id);

    Page<Category> search(String keys, Pageable pageable);
}
