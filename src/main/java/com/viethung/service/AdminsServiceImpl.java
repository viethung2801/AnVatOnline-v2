package com.viethung.service;

import com.viethung.dto.AdminDto;
import com.viethung.entity.ENUM.ERoleName;
import com.viethung.entity.Role;
import com.viethung.entity.User;
import com.viethung.repository.RoleRepository;
import com.viethung.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class AdminsServiceImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UploadFileServiceImpl uploadFileService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public boolean handleAdd(AdminDto adminDto) {
        User user = User.builder().build();
        List<Role> roles = roleRepository.findAll();

        try {
            // upload ảnh + set name mới
            if (!adminDto.getImageUrl().isEmpty()) {
                user.setImageUrl(uploadFileService.handleUpload(adminDto.getImageUrl()));
            }

            //mapTo User
            user.setCode(adminDto.getCode());
            user.setFirstName(adminDto.getFirstName());
            user.setLastName(adminDto.getLastName());
            user.setDateBirth(adminDto.getDateBirth());
            user.setAddress(adminDto.getAddress());
            user.setEmail(adminDto.getEmail());
            user.setPhoneNumber(adminDto.getPhoneNumber());
            user.setGender(adminDto.getGender());
            user.setPassword(passwordEncoder.encode(adminDto.getPassword()));
            user.setRoles(roles);

            //insert
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean handleUpdate(AdminDto adminDto) {
        User user =userRepository.findById(adminDto.getId()).orElse(null);

        try {
            // upload ảnh + set name mới
            if (!adminDto.getImageUrl().isEmpty()) {
                user.setImageUrl(uploadFileService.handleUpload(adminDto.getImageUrl()));
            }

            //mapTo User
            user.setCode(adminDto.getCode());
            user.setFirstName(adminDto.getFirstName());
            user.setLastName(adminDto.getLastName());
            user.setDateBirth(adminDto.getDateBirth());
            user.setAddress(adminDto.getAddress());
            user.setEmail(adminDto.getEmail());
            user.setPhoneNumber(adminDto.getPhoneNumber());
            user.setGender(adminDto.getGender());

            //insert
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public AdminDto findById(UUID id) {
        User user = userRepository.findById(id).orElse(null);
        return mapUserToAdminDto(user);
    }

    public AdminDto mapUserToAdminDto(User user) {
        AdminDto adminDto = AdminDto.builder().build();
        if (user == null) {
            return adminDto;
        }
        //mapping
        adminDto.setId(user.getId());
        adminDto.setCode(user.getCode());
        adminDto.setImageName(user.getImageUrl());
        adminDto.setFirstName(user.getFirstName());
        adminDto.setLastName(user.getLastName());
        adminDto.setGender(user.getGender());
        adminDto.setDateBirth(user.getDateBirth());
        adminDto.setEmail(user.getEmail());
        adminDto.setPhoneNumber(user.getPhoneNumber());
        adminDto.setAddress(user.getAddress());
        return adminDto;
    }

    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    public Page<User> findAllByKeys(Pageable pageable,String keys) {
        keys = "%"+keys+"%";
        return userRepository.searchByKeys(keys,pageable);
    }
}
