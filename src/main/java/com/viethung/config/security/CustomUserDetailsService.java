package com.viethung.config.security;

import com.viethung.entity.User;
import com.viethung.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Lấy username từ client nhập vào form
 * Chuyển đổi sang CustomUserDetails để xử lý
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username).orElse(null);
        System.out.println(user.toString());
        if (user == null){
            throw new UsernameNotFoundException("User name not found");
        }
        return CustomUserDetails.mapUserToUserDetail(user);
    }
}
