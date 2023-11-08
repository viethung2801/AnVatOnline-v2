package com.viethung.repository;

import com.viethung.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartDetailRepository extends JpaRepository<CartDetail, UUID> {
}
