package com.viethung.repository;

import com.viethung.entity.Cart;
import com.viethung.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    boolean existsByCode(String code);

    Cart findCartById(UUID id);

    Cart findByUser(User user);
}
