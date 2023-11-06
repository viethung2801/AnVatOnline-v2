package com.viethung.service;

import com.viethung.dto.CustomerDto;
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
public class CustomerServiceImpl {

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

    public boolean handleAdd(CustomerDto customerDto) {
        User user = User.builder().build();
        List<Role> roles = Arrays.asList(roleRepository.findByRoleName(ERoleName.ROLE_USER));

        try {
            // upload ảnh + set name mới
            if (!customerDto.getImageUrl().isEmpty()) {
                user.setImageUrl(uploadFileService.handleUpload(customerDto.getImageUrl()));
            }

            //mapTo User
            user.setCode(customerDto.getCode());
            user.setFirstName(customerDto.getFirstName());
            user.setLastName(customerDto.getLastName());
            user.setDateBirth(customerDto.getDateBirth());
            user.setAddress(customerDto.getAddress());
            user.setEmail(customerDto.getEmail());
            user.setPhoneNumber(customerDto.getPhoneNumber());
            user.setGender(customerDto.getGender());
            user.setPassword(passwordEncoder.encode(customerDto.getPassword()));
            user.setRoles(roles);

            //insert
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean handleUpdate(CustomerDto customerDto) {
        User user =userRepository.findById(customerDto.getId()).orElse(null);

        try {
            // upload ảnh + set name mới
            if (!customerDto.getImageUrl().isEmpty()) {
                user.setImageUrl(uploadFileService.handleUpload(customerDto.getImageUrl()));
            }

            //mapTo User
            user.setCode(customerDto.getCode());
            user.setFirstName(customerDto.getFirstName());
            user.setLastName(customerDto.getLastName());
            user.setDateBirth(customerDto.getDateBirth());
            user.setAddress(customerDto.getAddress());
            user.setEmail(customerDto.getEmail());
            user.setPhoneNumber(customerDto.getPhoneNumber());
            user.setGender(customerDto.getGender());

            //insert
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public CustomerDto findById(UUID id) {
        User user = userRepository.findById(id).orElse(null);
        return mapUserToCustomerDto(user);
    }

    public CustomerDto mapUserToCustomerDto(User user) {
        CustomerDto customerDto = CustomerDto.builder().build();
        if (user == null) {
            return customerDto;
        }
        //mapping
        customerDto.setId(user.getId());
        customerDto.setCode(user.getCode());
        customerDto.setImageName(user.getImageUrl());
        customerDto.setFirstName(user.getFirstName());
        customerDto.setLastName(user.getLastName());
        customerDto.setGender(user.getGender());
        customerDto.setDateBirth(user.getDateBirth());
        customerDto.setEmail(user.getEmail());
        customerDto.setPhoneNumber(user.getPhoneNumber());
        customerDto.setAddress(user.getAddress());
        return customerDto;
    }

    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    public Page<User> findAllByKeys(Pageable pageable,String keys) {
        keys = "%"+keys+"%";
        return userRepository.searchByKeys(keys,pageable);
    }
}
