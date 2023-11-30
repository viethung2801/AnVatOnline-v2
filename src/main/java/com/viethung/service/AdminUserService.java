package com.viethung.service;

import com.viethung.dto.AdminUserDto;
import com.viethung.dto.OrderDto;
import com.viethung.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AdminUserService {
    Page<User> findAll(Pageable pageable);

    //Đếm số bản ghi có code :code và có id khác với id update
    long countByCodeAndIdNot(String code, UUID id);

    long countByEmailAndIdNot(String email, UUID id);

    long countByPhoneNumberAndIdNot(String phoneNumber, UUID id);

    boolean existsByCode(String code);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean handleAdd(AdminUserDto adminUserDto);

    boolean handleUpdate(AdminUserDto adminUserDto);

    AdminUserDto findById(UUID id);

    User finUserById(UUID id);

    AdminUserDto mapUserToAdminUserDto(User user);

    User mapAdminUserToUser(AdminUserDto adminUserDto);

    void deleteById(UUID id);

    Page<User> findAllByKeys(Pageable pageable, String keys);

    Page<OrderDto> findAllOrderByUserId(UUID id, Pageable pageable);

    List<AdminUserDto> findByKeys(String keys);
}
