package com.viethung.service;

import com.viethung.dto.CartDetailDto;
import com.viethung.dto.MessageDto;
import com.viethung.entity.Cart;
import com.viethung.entity.CartDetail;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.UUID;

public interface CartService {
    List<CartDetailDto> findAllById(UUID cartId, HttpServletResponse response);

    List<CartDetailDto> findAllById(UUID cartId);

    List<CartDetailDto> findByUser(UUID userId);

    UUID getCartId(UUID userId);

    MessageDto updateCartDetail(UUID cartDetailId, int quantity);

    MessageDto deleteCartDetail(UUID cartDetailId);

    MessageDto addCartAnonymousUser(UUID productId,
                                    Integer qty,
                                    HttpServletRequest request,
                                    HttpServletResponse response);

    Cookie getCookie(String name, Cookie[] cookies);

    Cart newCartIdCookie(HttpServletResponse response);

    String generateCartCode();

    String generateCartDetailCode();

    CartDetailDto mapCartDetailToCartDetailDto(CartDetail cartDetail);

    CartDetail updateOrCreateCartDetail(Cart cart, UUID productId, Integer qty);

    MessageDto addCart(UUID userId, UUID productId, Integer qty);

    float getTotalPrice(List<CartDetailDto> cartDetailDtos);
}
