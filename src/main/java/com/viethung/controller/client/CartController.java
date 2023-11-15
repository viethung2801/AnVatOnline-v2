package com.viethung.controller.client;

import com.viethung.config.security.CustomUserDetails;
import com.viethung.dto.CartDetailDto;
import com.viethung.dto.MessageDto;
import com.viethung.dto.ProductCardDto;
import com.viethung.service.client.CartServiceImpl;
import com.viethung.service.client.ProductAllServiceImpl;
import com.viethung.service.client.ProductDetailServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
public class CartController {
    @Autowired
    private CartServiceImpl cartService;

    @Autowired
    private ProductDetailServiceImpl productDetailService;

    @GetMapping("/my-cart")
    public String displayUserCart(Authentication principal,
                                  HttpServletRequest request,
                                  HttpServletResponse response,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        List<CartDetailDto> cartDetailDtos;
        if (principal == null) {
            Cookie cookie = cartService.getCookie("cartId", request.getCookies());
            UUID cartId = UUID.fromString(cookie.getValue());
            cartDetailDtos = cartService.findAllById(cartId,response);
            model.addAttribute("cartId",cartId);
        } else {
//            cartDetailDtos = cartService.findAllByUser()
            CustomUserDetails customUserDetails = (CustomUserDetails) principal.getPrincipal();
            cartDetailDtos = cartService.findByUser(customUserDetails.getId());
            model.addAttribute("cartId",cartService.getCartId(customUserDetails.getId()));
        }

        //gợi ý
        List<ProductCardDto> productCardDtos = productDetailService.findTop8Related();
        float totalPrice = cartService.getTotalPrice(cartDetailDtos);
        model.addAttribute("cartDetailDtos", cartDetailDtos);
        model.addAttribute("productCardDtos", productCardDtos);
        model.addAttribute("totalPrice", totalPrice);
        return "client/page/cart";
    }

    @GetMapping("/add-cart/{productId}")
    public String onAddCart(@PathVariable Optional<UUID> productId,
                       @RequestParam Optional<Integer> qty,
                       Authentication principal,
                       HttpServletRequest request,
                       HttpServletResponse response,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        MessageDto message = null;
        if (principal == null) {
            message = cartService.addCartAnonymousUser(productId.orElse(null), qty.orElse(1), request, response);
        }else {
            CustomUserDetails customUserDetails = (CustomUserDetails) principal.getPrincipal();
            message = cartService.addCart(customUserDetails.getId(),productId.orElse(null), qty.orElse(1));
        }
        redirectAttributes.addFlashAttribute(message.getStatus(),message.getMessage());
        return "redirect:/my-cart";
    }
    @GetMapping("/update-cart/{cartDetailId}")
    public String onUpdateCart(@PathVariable Optional<UUID> cartDetailId,
                            @RequestParam Optional<Integer> qty,
                            RedirectAttributes redirectAttributes) {
        MessageDto message = cartService.updateCartDetail(cartDetailId.orElse(null),qty.orElse(1));
        redirectAttributes.addFlashAttribute(message.getStatus(),message.getMessage());
        return "redirect:/my-cart";
    }

    @GetMapping("/delete-cart/{cartDetailId}")
    public String onDeleteCart(@PathVariable Optional<UUID> cartDetailId,
                               RedirectAttributes redirectAttributes) {
        MessageDto message = cartService.deleteCartDetail(cartDetailId.orElse(null));
        redirectAttributes.addFlashAttribute(message.getStatus(),message.getMessage());
        return "redirect:/my-cart";
    }


}
