package com.viethung.service;

import com.viethung.dto.UserRegisterDto;
import com.viethung.entity.User;

public interface RegisterService {
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    User save(UserRegisterDto userRegisterDto);

    String generateUserCode();
}
