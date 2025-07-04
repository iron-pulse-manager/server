package com.fitness.common.security;

import com.fitness.domain.auth.entity.Auth;
import com.fitness.domain.auth.repository.AuthRepository;
import com.fitness.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Spring Security에서 사용자 정보를 로드하는 서비스
 * JWT 인증 과정에서 사용자 정보를 조회하고 권한을 설정
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthRepository authRepository;

    /**
     * 사용자명으로 사용자 정보를 로드
     * @param username 사용자명 (Auth 테이블의 username 필드)
     * @return UserDetails 구현체
     * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("사용자 정보 로드 시도: {}", username);
        
        Auth auth = authRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("사용자를 찾을 수 없습니다: {}", username);
                    return new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
                });

        User user = auth.getUser();
        
        log.debug("사용자 정보 로드 성공: {} (사용자 ID: {})", username, user.getUserId());
        
        return CustomUserPrincipal.builder()
                .userId(user.getUserId())
                .username(auth.getUsername())
                .password(auth.getPassword())
                .userType(user.getUserType())
                .authorities(getAuthorities(user))
                .enabled(user.getStatus().name().equals("ACTIVE"))
                .build();
    }

    /**
     * 사용자 권한 설정
     * UserType에 따라 해당 ROLE만 부여 (계층 구조 없음)
     */
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        switch (user.getUserType()) {
            case OWNER:
                authorities.add(new SimpleGrantedAuthority("ROLE_OWNER"));
                break;
            case EMPLOYEE:
                authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
                break;
            case MEMBER:
                authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
                break;
            default:
                log.warn("알 수 없는 사용자 타입: {}", user.getUserType());
                break;
        }
        
        log.debug("사용자 권한 설정 완료: {} -> {}", user.getUserType(), authorities);
        return authorities;
    }
}