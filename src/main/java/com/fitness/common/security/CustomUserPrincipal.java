package com.fitness.common.security;

import com.fitness.common.enums.UserType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Spring Security에서 사용하는 사용자 정보 클래스
 * UserDetails 인터페이스를 구현하여 인증 및 권한 정보 제공
 */
@Getter
@Builder
public class CustomUserPrincipal implements UserDetails {
    
    private final Long userId;
    private final String username;
    private final String password;
    private final UserType userType;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return enabled;
    }
}