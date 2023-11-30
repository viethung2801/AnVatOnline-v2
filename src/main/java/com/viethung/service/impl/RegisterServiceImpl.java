package com.viethung.service.impl;

import com.viethung.dto.UserRegisterDto;
import com.viethung.entity.ENUM.ERoleName;
import com.viethung.entity.Role;
import com.viethung.entity.User;
import com.viethung.repository.RoleRepository;
import com.viethung.repository.UserRepository;
import com.viethung.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public User save(UserRegisterDto userRegisterDto) {
        // kiểm tra code trùng
        String userCode = generateUserCode();
        Role role = roleRepository.findByRoleName(ERoleName.ROLE_USER);
        if (role == null){
            roleRepository.save(Role.builder().roleName(ERoleName.ROLE_USER).build());
            Role roleAdmin = roleRepository.findByRoleName(ERoleName.ROLE_ADMIN);
            if (roleAdmin == null){
                roleRepository.save(Role.builder().roleName(ERoleName.ROLE_ADMIN).build());
            }
        }
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

    @Override
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
