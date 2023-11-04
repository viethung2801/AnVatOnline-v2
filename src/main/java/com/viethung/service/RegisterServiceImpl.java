package com.viethung.service;

import com.viethung.dto.UserRegisterDto;
import com.viethung.entity.ENUM.ERoleName;
import com.viethung.entity.Role;
import com.viethung.entity.User;
import com.viethung.repository.RoleRepository;
import com.viethung.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RegisterServiceImpl {
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    public User save(UserRegisterDto userRegisterDto) {
        // kiểm tra code trùng
        String userCode = generateUserCode();
        Role role = roleRepository.findByRoleName(ERoleName.ROLE_USER);
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(role);
        User userSave = User.builder()
                .code(userCode)
                .firstName(userRegisterDto.getFirstName())
                .lastName(userRegisterDto.getLastName())
                .email(userRegisterDto.getEmail())
                .phoneNumber(userRegisterDto.getPhoneNumber())
                .password(passwordEncoder.encode(userRegisterDto.getPassword()))
                .roles(roles)
                .build();
        return userRepository.save(userSave);
    }

    public String generateUserCode() {
        long number = userRepository.count() + 1;// != 00
        while (true) {
            if (userRepository.existsByCode("US0" + number)) {
                number++;
            }
            break;
        }
        return "US0" + number;
    }
}
