package com.viethung.controller.client;

import com.viethung.dto.OrderDto;
import com.viethung.service.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class OrderDetailController {
    @Autowired
    private OrderServiceImpl orderService;

//    @GetMapping("/order-detail")
//    public String displayView(Model model){
//        return "client/page/order-detail";
//    }
    @GetMapping("/order-detail/{orderId}")
    public String cancelOrder(Model model,
                                     @PathVariable UUID orderId) {
        OrderDto orderDto = orderService.findOrderById(orderId);
        model.addAttribute("orderDto", orderDto);
        return "client/page/order-detail";
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
        return "redirect:/order-detail/"+orderId.toString();
    }
}
