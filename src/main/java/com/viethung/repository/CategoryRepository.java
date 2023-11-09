package com.viethung.repository;

import com.viethung.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsByCode(String code);

    @Query("select c from Category c where c.code like :keys or c.name like :keys")
    Page<Category> searchByKeys(String keys, Pageable pageable);

    boolean existsByCodeAndIdNot(String code, UUID id);
}
