package com.viethung.controller.admin;

import com.viethung.dto.OrderDetailDto;
import com.viethung.dto.OrderDto;
import com.viethung.service.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class PosController {
    private OrderServiceImpl orderService;

    @Autowired
    public void setOrderService(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/pos")
    public String displayOrderProcess(@RequestParam Optional<Integer> page,
                                      Model model) {
        //get order process
        List<OrderDto> orderDtos = orderService.getOrderProcess();
        model.addAttribute("orderDtos",orderDtos);
        return "admin/page/pos-list";
    }

    @GetMapping("/pos/create/new")
    public String newOrder(Model model) {
        return "admin/page/pos-detail";
    }

    @GetMapping("/pos/{orderId}")
    public String detailOrder(Model model, @PathVariable Optional<UUID> orderId) {
        OrderDto orderDto = orderService.findOrderById(orderId.orElse(null));

        model.addAttribute("orderDto",orderDto);
        System.out.println(orderDto);
        return "admin/page/pos-detail";
    }
}
