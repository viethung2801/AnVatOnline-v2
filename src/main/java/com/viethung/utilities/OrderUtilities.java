package com.viethung.utilities;

import com.viethung.repository.OrderDetailRepository;
import com.viethung.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderUtilities {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    public String generateOrderCode() {
        long num = orderRepository.count() + 1;
        while (true) {
            if (!orderRepository.existsByCode("HD0" + num)) {
                break;
            }
            num++;
        }
        return "HD0" + num;
    }

    public String generateOrderDetailCode() {
        long num = orderDetailRepository.count() + 1;
        while (true) {
            if (!orderDetailRepository.existsByCode("OD0" + num)) {
                break;
            }
            num++;
        }
        return "OD0" + num;
    }
}
