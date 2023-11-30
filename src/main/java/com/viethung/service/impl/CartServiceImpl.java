package com.viethung.service.impl;

import com.viethung.dto.CartDetailDto;
import com.viethung.dto.MessageDto;
import com.viethung.entity.Cart;
import com.viethung.entity.CartDetail;
import com.viethung.entity.User;
import com.viethung.repository.CartDetailRepository;
import com.viethung.repository.CartRepository;
import com.viethung.repository.ProductRepository;
import com.viethung.repository.UserRepository;
import com.viethung.service.CartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<CartDetailDto> findAllById(UUID cartId, HttpServletResponse response) {
        List<CartDetailDto> cartDetailDtos = new ArrayList<>();
        Cart cart = cartRepository.findCartById(cartId);
        if (cart == null){
            cart  = newCartIdCookie(response);
        }
        if (cart.getCartDetails() != null){
            cart.getCartDetails().forEach(cartDetail ->
                    cartDetailDtos.add(mapCartDetailToCartDetailDto(cartDetail))
            );
        }
        return cartDetailDtos;
    }

    @Override
    public List<CartDetailDto> findAllById(UUID cartId) {
        List<CartDetailDto> cartDetailDtos = new ArrayList<>();
        Cart cart = cartRepository.findCartById(cartId);
        cart.getCartDetails().forEach(cartDetail ->
                cartDetailDtos.add(mapCartDetailToCartDetailDto(cartDetail))
        );
        return cartDetailDtos;
    }

    @Override
    public List<CartDetailDto> findByUser(UUID userId) {
        User user = userRepository.findUserById(userId);
        List<CartDetailDto> cartDetailDtos = new ArrayList<>();
        Cart cart = cartRepository.findByUser(user);
        if (cart != null && cart.getCartDetails() != null) {
            cart.getCartDetails().forEach(cartDetail ->
                    cartDetailDtos.add(mapCartDetailToCartDetailDto(cartDetail))
            );
        }

        return cartDetailDtos;
    }

    @Override
    public UUID getCartId(UUID userId){
        User user = userRepository.findUserById(userId);
        Cart cart = cartRepository.findByUser(user);
        return cart.getId();
    }

    @Override
    public MessageDto updateCartDetail(UUID cartDetailId, int quantity) {
        try {
            CartDetail cartDetail = cartDetailRepository.findById(cartDetailId).get();
            cartDetail.setQuantity(quantity);
            cartDetailRepository.save(cartDetail);
            return MessageDto.builder().status("success").message("Cập nhật thành công").build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return MessageDto.builder().status("fail").message("Đã xảy ra lỗi! Vui lòng thử lại").build();
    }

    @Override
    public MessageDto deleteCartDetail(UUID cartDetailId) {
        try {
            cartDetailRepository.deleteById(cartDetailId);
            return MessageDto.builder().status("success").message("Xóa thành công").build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return MessageDto.builder().status("fail").message("Đã xảy ra lỗi! Vui lòng thử lại").build();
    }

    @Override
    public MessageDto addCartAnonymousUser(UUID productId,
                                           Integer qty,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        // kiểm tra đã tồn tại cookie cartId chưa
        Cookie[] cookies = request.getCookies();
        UUID cartId;
        if (getCookie("cartId", cookies) == null) {
            // chưa có => tạo mới 1 cookie
            cartId = newCartIdCookie(response).getId();
        } else {
            // có rồi => lấy cart by id
            Cookie cookie = getCookie("cartId", cookies);
            System.out.println("MaxAge: " + cookie.getMaxAge());
            cartId = UUID.fromString(cookie.getValue());
        }
        Cart cart = cartRepository.findCartById(cartId);
        try {
            CartDetail cartDetail = updateOrCreateCartDetail(cart, productId, qty);
            cartDetailRepository.save(cartDetail);

            return MessageDto.builder().status("success").message("Thêm thành công").build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MessageDto.builder().status("fail").message("Thêm thất bại").build();
    }

    @Override
    public Cookie getCookie(String name, Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }

        return null;
    }

    @Override
    public Cart newCartIdCookie(HttpServletResponse response) {
        Cart cart = Cart.builder().build();
        cart.setCode(generateCartCode());
        cart.setCreatedDate(LocalDateTime.now());
        cart.setUser(null);


        String name = "cartId";
        String value = cartRepository.save(cart).getId().toString();

        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(30 * 24 * 60 * 60);//30 days = 30 * 24 * 60 * 60 or -1 = session
        System.out.println(value);
        response.addCookie(cookie);

        return cart;
    }

    @Override
    public String generateCartCode() {
        long num = cartRepository.count();
        while (true) {
            if (!cartRepository.existsByCode("CA0" + num)) {
                break;
            }
            num++;
        }
        return "CA0" + num;
    }

    @Override
    public String generateCartDetailCode() {
        long num = cartDetailRepository.count() + 1;
        while (true) {
            if (!cartDetailRepository.existsByCode("CD0" + num)) {
                break;
            }
            num++;
        }
        return "CD0" + num;
    }

    @Override
    public CartDetailDto mapCartDetailToCartDetailDto(CartDetail cartDetail) {
        CartDetailDto cartDetailDto = CartDetailDto.builder().build();

        //mapper
        cartDetailDto.setId(cartDetail.getId());
        cartDetailDto.setImageName(cartDetail.getProduct().getProductImages().get(0).getUrl());
        cartDetailDto.setProductName(cartDetail.getProduct().getName());
        cartDetailDto.setPrice(cartDetail.getProduct().getPrice());
        cartDetailDto.setQuantity(cartDetail.getQuantity());

        return cartDetailDto;
    }

    @Override
    public CartDetail updateOrCreateCartDetail(Cart cart, UUID productId, Integer qty) {

        if (cart.getCartDetails() != null) {
            for (CartDetail cartDetailTemp : cart.getCartDetails()) {
                if (cartDetailTemp.getProduct().getId().equals(productId)) {
                    cartDetailTemp.setQuantity(cartDetailTemp.getQuantity() + qty);
                    return cartDetailTemp;
                }
            }
        }

        CartDetail cartDetail = CartDetail.builder().build();

        cartDetail.setCode(generateCartDetailCode());
        cartDetail.setCart(cart);
        cartDetail.setCreatedDate(LocalDateTime.now());
        cartDetail.setProduct(productRepository.findProductById(productId));
        cartDetail.setQuantity(qty);

        return cartDetail;
    }

    @Override
    public MessageDto addCart(UUID userId, UUID productId, Integer qty) {
        if (userId == null || productId == null) {
            return MessageDto.builder().message("fail").message("Có lỗi xảy ra! Vui lòng thử lại").build();
        }
        // get cart
        User user = userRepository.findUserById(userId);
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = Cart.builder()
                    .code(generateCartCode())
                    .user(user)
                    .createdDate(LocalDateTime.now())
                    .build();
            cart = cartRepository.save(cart);
        }

        try {
            CartDetail cartDetail = updateOrCreateCartDetail(cart, productId, qty);
            cartDetailRepository.save(cartDetail);
            return MessageDto.builder().status("success").message("Thêm thành công").build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MessageDto.builder().status("fail").message("Thêm thất bại").build();
    }
    @Override
    public float getTotalPrice(List<CartDetailDto> cartDetailDtos) {
        float value = 0;
        for (CartDetailDto cartDetailDto : cartDetailDtos) {
            value += (cartDetailDto.getPrice().intValue() * cartDetailDto.getQuantity());
        }
        return value;
    }
}
