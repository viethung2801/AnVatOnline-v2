package com.viethung.service.impl;

import com.viethung.dto.ChangePasswordDto;
import com.viethung.entity.User;
import com.viethung.repository.UserRepository;
import com.viethung.service.ChangePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ChangePasswordServiceImpl implements ChangePasswordService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean handleChangePassword(ChangePasswordDto changePasswordDto, Principal principal) {
        User user = userRepository.findUserByEmail(principal.getName()).orElse(null);
        if (user == null) {
            return false;
        }
        try {
            user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
