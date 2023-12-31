package com.viethung.service.impl;

import com.viethung.dto.CartDetailDto;
import com.viethung.dto.CheckoutDto;
import com.viethung.dto.MailModel;
import com.viethung.entity.CartDetail;
import com.viethung.entity.ENUM.EOrderState;
import com.viethung.entity.ENUM.EOrderStatus;
import com.viethung.entity.Order;
import com.viethung.entity.OrderDetail;
import com.viethung.entity.Product;
import com.viethung.entity.User;
import com.viethung.repository.CartDetailRepository;
import com.viethung.repository.OrderDetailRepository;
import com.viethung.repository.OrderRepository;
import com.viethung.repository.ProductRepository;
import com.viethung.repository.UserRepository;
import com.viethung.service.CartService;
import com.viethung.service.CheckoutService;
import com.viethung.service.MailService;
import com.viethung.utilities.OrderUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService {
    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private OrderUtilities orderUtilities;

    @Override
    public CartDetailDto newCartDetailDto(UUID productId, Integer quantity) {
        Product product = productRepository.findProductById(productId);

        CartDetail cartDetail = CartDetail.builder().build();
        cartDetail.setCode(cartService.generateCartDetailCode());
        cartDetail.setCreatedDate(LocalDateTime.now());
        cartDetail.setQuantity(quantity);
        cartDetail.setProduct(product);
        cartDetail.setCart(null);

        cartDetail = cartDetailRepository.save(cartDetail);
        CartDetailDto cartDetailDto = cartService.mapCartDetailToCartDetailDto(cartDetail);

        return cartDetailDto;
    }

    @Override
    public String handleCheckout(CheckoutDto checkoutDto, UUID userId) {
        User user = null;
        if (userId != null) {
            user = userRepository.findUserById(userId);
        }
        Order order = mapCheckoutDtoToOrder(checkoutDto, user);
        //save order
        order = orderRepository.save(order);
        //save order detail
        for (UUID cartDetailId : checkoutDto.getCartDetailIds()) {
            // chuyển tử cart detail sang orderDetail và xóa CartDetail
            CartDetail cartDetail = cartDetailRepository.findById(cartDetailId).get();

            OrderDetail orderDetail = mapCartDetailToOrderDetail(order, cartDetail);

            orderDetailRepository.save(orderDetail);

            cartDetailRepository.delete(cartDetail);
        }
        //gửi email cho người dùng để theo dõi đơn hàng
        sendOrderDetail(order);
        return order.getId().toString();
    }

    @Override
    public void sendOrderDetail(Order order) {
        try {
            String to = order.getEmail();
            String subject = "Ăn Vặt Online! Theo dõi đơn hàng";
            String body = "Cảm ơn bạn đã đặt hàng<br />\n" +
                    "      Theo dõi đơn hàng của bạn <a href=\"http://localhost:8080/order-detail/"+order.getId()+"\">Tại đây</a>";
            //Gửi email
            MailModel mailModel = MailModel.builder().build();
            mailModel.setTo(to);
            mailModel.setSubject(subject);
            mailModel.setBody(body);
            mailService.sendEmail(mailModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Order mapCheckoutDtoToOrder(CheckoutDto checkoutDto, User user) {
        Order order = Order.builder().build();
        //map field
        order.setCode(orderUtilities.generateOrderCode());
        order.setCreatedDate(LocalDateTime.now());
        order.setStatus(EOrderStatus.ORDERED);//cờ xác nhận
        order.setState(EOrderState.PROCESS);//đang xử lý
        order.setReceiverName(checkoutDto.getName());
        order.setPhoneNumber(checkoutDto.getPhoneNumber());
        if (user == null) {
            //anonymous user follow order by this email
            order.setEmail(checkoutDto.getEmail());

        } else {
            order.setEmail(user.getEmail());
        }
        order.setAddress(checkoutDto.getAddress());
        order.setNote(checkoutDto.getNote());
        order.setUser(user);

        return order;
    }

    @Override
    public OrderDetail mapCartDetailToOrderDetail(Order order, CartDetail cartDetail) {
        OrderDetail orderDetail = OrderDetail.builder().build();
        //map field
        orderDetail.setCode(orderUtilities.generateOrderDetailCode());
        orderDetail.setOrder(order);
        orderDetail.setCreatedDate(LocalDateTime.now());
        orderDetail.setQuantity(cartDetail.getQuantity());
        orderDetail.setPrice(cartDetail.getProduct().getPrice());
        orderDetail.setPriceSale(cartDetail.getProduct().getPrice());
        orderDetail.setProduct(cartDetail.getProduct());

        return orderDetail;
    }


}
