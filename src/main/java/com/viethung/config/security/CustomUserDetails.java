package com.viethung.config.security;

import com.viethung.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Khi người dùng login sẽ tạo ra 1 UserDetail
 * Lấy từ UserDetail lấy ra username & password khi nhập
 * Chuyển đổi sang UserDetail mà có field mình muốn
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomUserDetails implements UserDetails {
    private UUID id;
    private String username;
    private String imageUrl;
    private String lastName;
    private String firstName;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public static CustomUserDetails mapUserToUserDetail(User user) {
        List<? extends GrantedAuthority> grantedAuthorities =
                user.getRoles().stream().map(
                        role -> new SimpleGrantedAuthority(role.getRoleName().toString())
                ).toList();
        return CustomUserDetails.builder()
                .username(user.getEmail())
                .authorities(grantedAuthorities)
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .imageUrl(user.getImageUrl())
                .build();
    }

    public CustomUserDetails(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
