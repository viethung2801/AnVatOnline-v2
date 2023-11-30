package com.viethung.restcontroller;

import com.viethung.dto.OrderDetailDto;
import com.viethung.service.OrderDetailService;
import com.viethung.service.impl.OrderDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class OrderDetailRestController {

    private OrderDetailService orderDetailService;

    @Autowired
    public void setOrderDetailService(OrderDetailServiceImpl orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    // create
    @PostMapping("/order-details")
    public ResponseEntity<OrderDetailDto> onCreateOrderDetail(@RequestBody OrderDetailDto orderDetailDto) {
        orderDetailDto = orderDetailService.saveOrderDetail(orderDetailDto);
        if (orderDetailDto == null){
            throw new RuntimeException("Có lỗi xảy ra vui lòng thử lại");
        }
        return ResponseEntity.ok(orderDetailDto);
    }
    //update
    @PutMapping("/order-details")
    public ResponseEntity<OrderDetailDto> onUpdateOrderDetail(@RequestBody OrderDetailDto orderDetailDto) {
        orderDetailDto = orderDetailService.updateOrderDetail(orderDetailDto);
        if (orderDetailDto == null){
            throw new RuntimeException("Có lỗi xảy ra vui lòng thử lại");
        }
        return ResponseEntity.ok(orderDetailDto);
    }
    @DeleteMapping("/order-details/{orderDetailId}")
    public ResponseEntity<OrderDetailDto> onDeleteOrderDetail(@PathVariable Optional<UUID> orderDetailId) {
        OrderDetailDto orderDetailDto = orderDetailService.deleteOrderDetail(orderDetailId.orElse(null));
        if (orderDetailDto == null){
            throw new RuntimeException("Có lỗi xảy ra vui lòng thử lại");
        }
        return ResponseEntity.ok(orderDetailDto);
    }
    //delete

    @ExceptionHandler
    public ResponseEntity<?> handleException(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
