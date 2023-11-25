package com.viethung.service;

import com.viethung.dto.AdminUserDto;
import com.viethung.dto.OrderDto;
import com.viethung.entity.ENUM.ERoleName;
import com.viethung.entity.Order;
import com.viethung.entity.Role;
import com.viethung.entity.User;
import com.viethung.repository.OrderRepository;
import com.viethung.repository.RoleRepository;
import com.viethung.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class AdminUserServiceImpl {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UploadFileServiceImpl uploadFileService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderServiceImpl orderService;

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    //Đếm số bản ghi có code :code và có id khác với id update
    public long countByCodeAndIdNot(String code, UUID id) {
        return userRepository.countByCodeAndIdNot(code, id);
    }

    public long countByEmailAndIdNot(String email, UUID id) {
        return userRepository.countByEmailAndIdNot(email, id);
    }

    public long countByPhoneNumberAndIdNot(String phoneNumber, UUID id) {
        return userRepository.countByPhoneNumberAndIdNot(phoneNumber, id);
    }

    public boolean existsByCode(String code) {
        return userRepository.existsByCode(code);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    public boolean handleAdd(AdminUserDto adminUserDto) {

        try {
            // upload ảnh + set name mới
            if (!adminUserDto.getImageUrl().isEmpty()) {
                adminUserDto.setImageName(uploadFileService.handleUpload(adminUserDto.getImageUrl()));
            }
            //mapTo User
            User user = mapAdminUserToUser(adminUserDto);
            //insert
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean handleUpdate(AdminUserDto adminUserDto) {
        User user = userRepository.findById(adminUserDto.getId()).orElse(null);

        try {
            // upload ảnh + set name mới
            if (!adminUserDto.getImageUrl().isEmpty()) {
                user.setImageUrl(uploadFileService.handleUpload(adminUserDto.getImageUrl()));
            }
            //set field
            user.setCode(adminUserDto.getCode());
            user.setFirstName(adminUserDto.getFirstName());
            user.setLastName(adminUserDto.getLastName());
            user.setDateBirth(adminUserDto.getDateBirth());
            user.setGender(adminUserDto.getGender());
            user.setEmail(adminUserDto.getEmail());
            user.setPhoneNumber(adminUserDto.getPhoneNumber());
            user.setAddress(adminUserDto.getAddress());
            //Roles

            if (adminUserDto.getPosition() == 0) {
                // Khách hàng
                user.getRoles().clear(); // Xóa tất cả các quyền hiện tại
                user.getRoles().add(roleRepository.findByRoleName(ERoleName.ROLE_USER)); // Xóa tất cả các quyền
            }
            if (adminUserDto.getPosition() == 1) {
                // Admin
                user.getRoles().clear(); // Xóa tất cả các quyền hiện tại
                user.getRoles().addAll(roleRepository.findAll()); // Thêm tất cả các quyền mới
            }
            //insert
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public AdminUserDto findById(UUID id) {
        User user = userRepository.findById(id).orElse(null);
        return mapUserToAdminUserDto(user);
    }
    public User finUserById(UUID id) {
       return userRepository.findById(id).orElse(null);
    }

    public AdminUserDto mapUserToAdminUserDto(User user) {
        AdminUserDto adminUserDto = AdminUserDto.builder().build();
        if (user == null) {
            return adminUserDto;
        }
        //mapping
        adminUserDto.setId(user.getId());
        adminUserDto.setCode(user.getCode());
        adminUserDto.setImageName(user.getImageUrl());
        adminUserDto.setFirstName(user.getFirstName());
        adminUserDto.setLastName(user.getLastName());
        adminUserDto.setGender(user.getGender());
        adminUserDto.setDateBirth(user.getDateBirth());
        adminUserDto.setEmail(user.getEmail());
        adminUserDto.setPhoneNumber(user.getPhoneNumber());
        adminUserDto.setAddress(user.getAddress());
        adminUserDto.setPosition(user.getRoles().size() == 1 ? 0 : 1);//0: khách hàng , 1: Amdin
        return adminUserDto;
    }

    public User mapAdminUserToUser(AdminUserDto adminUserDto) {
        User user = User.builder().build();
        //mapTo User
        user.setId(adminUserDto.getId());
        user.setCode(adminUserDto.getCode());
        user.setImageUrl(adminUserDto.getImageName());
        user.setFirstName(adminUserDto.getFirstName());
        user.setLastName(adminUserDto.getLastName());
        user.setDateBirth(adminUserDto.getDateBirth());
        user.setAddress(adminUserDto.getAddress());
        user.setEmail(adminUserDto.getEmail());
        user.setPhoneNumber(adminUserDto.getPhoneNumber());
        user.setGender(adminUserDto.getGender());
        if (!adminUserDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(adminUserDto.getPassword()));
        }
        if (adminUserDto.getPosition() == 0) {
            //khách hàng
            user.setRoles(List.of(roleRepository.findByRoleName(ERoleName.ROLE_USER)));
        }
        if (adminUserDto.getPosition() == 1) {
            //Admin
            user.setRoles(roleRepository.findAll());
        }

        return user;
    }

    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    public Page<User> findAllByKeys(Pageable pageable, String keys) {
        keys = "%" + keys + "%";
        return userRepository.searchByKeys(keys, pageable);
    }

    public Page<OrderDto> findAllOrderByUserId(UUID id, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllOrderByUser(id,pageable);
        List<OrderDto> orderDtos = new ArrayList<>();
        orders.forEach(order -> orderDtos.add(orderService.mapOrderToOrderDto(order)));
        return new PageImpl<OrderDto>(orderDtos,pageable,orders.getTotalElements());
    }

    public List<AdminUserDto> findByKeys(String keys){
//        keys = "%"+keys+"%";
        List<User> users = userRepository
                .findTop7ByCodeLikeOrFirstNameLikeOrLastNameLikeOrEmailLikeOrPhoneNumberLike(
                        keys,keys,keys,keys,keys
                );

        List<AdminUserDto> adminUserDtos = users.stream().map(this::mapUserToAdminUserDto).toList();
        return adminUserDtos;
    }
}
