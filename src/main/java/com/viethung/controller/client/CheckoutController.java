package com.viethung.controller.client;

import com.viethung.config.security.CustomUserDetails;
import com.viethung.dto.CartDetailDto;
import com.viethung.dto.CheckoutDto;
import com.viethung.entity.CartDetail;
import com.viethung.service.client.CartServiceImpl;
import com.viethung.service.client.CheckoutServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class CheckoutController {
    @Autowired
    private CheckoutServiceImpl checkoutService;

    @Autowired
    private CartServiceImpl cartService;

    //form checkout
    @GetMapping("/checkout")
    public String displayCheckoutDetail(@RequestParam("productId") Optional<UUID> productId,
                                        @RequestParam("qty") Optional<Integer> quantity,
                                        Model model,
                                        RedirectAttributes redirectAttributes) {
        //product is null
        if (productId.orElse(null) == null) {
            redirectAttributes.addFlashAttribute("fail", "Vui lòng thử lại");
            return "redirect:/my-cart";
        }
        CartDetailDto cartDetailDto = checkoutService.newCartDetailDto(productId.get(), quantity.orElse(0));
        List<CartDetailDto> cartDetailDtos = List.of(cartDetailDto);
        float totalPrice = cartService.getTotalPrice(cartDetailDtos);
        model.addAttribute("checkoutDto", CheckoutDto.builder().build());
        model.addAttribute("cartDetailDtos", cartDetailDtos);
        model.addAttribute("totalPrice", totalPrice);
        return "client/page/checkout";
    }

    @GetMapping("/checkout/{cartId}")
    public String displayCheckoutDetail(Model model,
                                        RedirectAttributes redirectAttributes,
                                        @PathVariable UUID cartId) {

        List<CartDetailDto> cartDetailDtos = cartService.findAllById(cartId);
        float totalPrice = cartService.getTotalPrice(cartDetailDtos);

        model.addAttribute("checkoutDto", CheckoutDto.builder().build());
        model.addAttribute("cartDetailDtos", cartDetailDtos);
        model.addAttribute("totalPrice", totalPrice);
        return "client/page/checkout";
    }

    @PostMapping("/checkout")
    public String onOrder(@Valid @ModelAttribute CheckoutDto checkoutDto,
                          Authentication principal,
                          BindingResult result,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (principal == null && checkoutDto.getEmail().isEmpty()) {
            result.rejectValue("email", "invalid", "Email không được trống");
        }
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("fail", "Đã xảy ra lỗi vui lòng thử lại");
            return "redirect:/my-order";
        }
        try {
            UUID userId = null;
            if (principal != null){//người dùng ẩn danh
                CustomUserDetails customUserDetails = (CustomUserDetails) principal.getPrincipal();
                userId = customUserDetails.getId();
            }

            String orderId = checkoutService.handleCheckout(checkoutDto, userId);
            if (orderId != null) {
                // đặt hàng thành công
                model.addAttribute("orderId",orderId);
                return "client/page/order-success";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("fail", "Đã xảy ra lỗi vui lòng thử lại");
        return "redirect:/my-cart";
    }
}
