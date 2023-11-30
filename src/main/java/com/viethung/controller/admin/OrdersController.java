package com.viethung.controller.admin;

import com.viethung.dto.OrderDto;
import com.viethung.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class OrdersController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String displayOrderList(@RequestParam Optional<Integer> page,
                                   Model model) {
        Pageable pageable = PageRequest.of(page.orElse(0), 50, Sort.by("createdDate").descending());

        Page<OrderDto> orderDtos = orderService.findAllOrderDto(pageable);
        model.addAttribute("orderDtos", orderDtos);
        return "admin/page/orders";
    }

    @GetMapping("/orders/search")
    public String onSearch(@RequestParam Optional<Integer> page,
                           @RequestParam Optional<String> keys,
                           @RequestParam Optional<Integer> status,
                           @RequestParam Optional<Integer> state,
                                   Model model) {
        Pageable pageable = PageRequest.of(page.orElse(0), 50, Sort.by("createdDate").descending());
        Page<OrderDto> orderDtos = orderService.search(
                keys.orElse(""),
                status.orElse(-1),
                state.orElse(-1),
                pageable);
        model.addAttribute("orderDtos", orderDtos);
        return "admin/page/orders";
    }

    @GetMapping("/order-detail/{orderId}")
    public String displayOrderDetail(Model model,
                                     @PathVariable UUID orderId) {
        OrderDto orderDto = orderService.findOrderById(orderId);
        model.addAttribute("orderDto", orderDto);
        return "admin/page/order-detail";
    }

    @PostMapping("/order/{orderId}")
    public String onChangeStatus(RedirectAttributes redirectAttributes,
                                 @PathVariable UUID orderId,
                                 @RequestParam Integer status) {
        try {
            boolean isUpdate = orderService.handleUpdateStatus(orderId, status);
            if (isUpdate) {
                redirectAttributes.addFlashAttribute("success", "Cập nhật thành công");
            } else {
                redirectAttributes.addFlashAttribute("fail", "Cập nhật thất bại");
            }
            return "redirect:/admin/order-detail/"+orderId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("fail", "Cập nhật thất bại");
        return "redirect:/admin/order-detail/"+orderId;
    }
    @GetMapping("/cancel-order/{orderId}")
    public String displayOrderDetail(RedirectAttributes redirectAttributes,
                                     @PathVariable UUID orderId) {
        boolean isCancel = orderService.onCancelOrder(orderId);
        if (isCancel){
            redirectAttributes.addFlashAttribute("success","Hủy thành công");
        }else {
            redirectAttributes.addFlashAttribute("fail","Hủy thất bại");
        }
        return "redirect:/admin/order-detail/"+orderId.toString();
    }

}
