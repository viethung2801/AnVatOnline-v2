package com.viethung.restcontroller;

import com.viethung.dto.AdminUserDto;
import com.viethung.dto.OrderDto;
import com.viethung.service.AdminUserService;
import com.viethung.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {
    private AdminUserService adminUserService;
    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setAdminUserService(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping("/users/top7")
    public ResponseEntity<List<AdminUserDto>> find7Product(@RequestParam(defaultValue = "") String keys) {
        List<AdminUserDto> adminUserDtos = adminUserService.findByKeys(keys);
        return ResponseEntity.ok(adminUserDtos);
    }

    @PutMapping("/orders")
    public ResponseEntity<OrderDto> updateUserInOrder(@RequestBody OrderDto orderDto) {
        OrderDto orderDtoTemp = orderService.updateUserInOrder(orderDto);
        if (orderDtoTemp == null){
            throw new RuntimeException("Có lỗi xảy ra vui lòng thử lại");
        }
        return null;
    }

//    @ExceptionHandler
//    public ResponseEntity<?> handleException(Exception e){
//        return ResponseEntity.badRequest().body(e.getMessage());
//    }
}
