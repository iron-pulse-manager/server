package com.fitness.domain.auth.service;

import com.fitness.domain.auth.dto.*;
import com.fitness.common.enums.SocialProvider;
import com.fitness.common.enums.UserType;
import com.fitness.common.jwt.JwtUtil;
import com.fitness.common.security.CustomUserPrincipal;
import com.fitness.domain.auth.entity.Auth;
import com.fitness.domain.auth.repository.AuthRepository;
import com.fitness.domain.user.entity.User;
import com.fitness.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 소셜 로그인 처리 서비스
 * 카카오, 애플 로그인을 통한 회원가입 및 로그인 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SocialLoginService {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    /**
     * 소셜 로그인 처리
     * @param request 소셜 로그인 요청
     * @return 로그인 응답
     */
    public LoginResponse socialLogin(SocialLoginRequest request) {
        try {
            // 소셜 제공자별 사용자 정보 조회
            SocialUserInfo socialUserInfo = getSocialUserInfo(request);

            // 기존 사용자 확인 또는 신규 사용자 생성
            Auth auth = findOrCreateUser(socialUserInfo, request.getProvider());

            // JWT 토큰 생성
            String accessToken = generateAccessToken(auth);
            String refreshToken = jwtUtil.generateRefreshToken(auth.getUsername());

            log.info("소셜 로그인 성공 - 제공자: {}, 사용자: {}, 타입: {}", 
                    request.getProvider(), auth.getUsername(), auth.getUser().getUserType());

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .userId(auth.getUser().getUserId())
                    .username(auth.getUsername())
                    .userType(auth.getUser().getUserType())
                    .expiresIn(900L) // 15분
                    .build();

        } catch (Exception e) {
            log.error("소셜 로그인 실패 - 제공자: {}, 원인: {}", request.getProvider(), e.getMessage());
            throw new RuntimeException("소셜 로그인에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 소셜 제공자별 사용자 정보 조회
     */
    private SocialUserInfo getSocialUserInfo(SocialLoginRequest request) {
        switch (request.getProvider()) {
            case KAKAO:
                return getKakaoUserInfo(request.getAccessToken());
            case APPLE:
                return getAppleUserInfo(request);
            default:
                throw new IllegalArgumentException("지원하지 않는 소셜 제공자입니다: " + request.getProvider());
        }
    }

    /**
     * 카카오 사용자 정보 조회
     */
    private SocialUserInfo getKakaoUserInfo(String accessToken) {
        try {
            String url = "https://kapi.kakao.com/v2/user/me";
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, KakaoUserInfo.class);
            
            KakaoUserInfo kakaoUserInfo = response.getBody();
            if (kakaoUserInfo == null) {
                throw new RuntimeException("카카오 사용자 정보를 가져올 수 없습니다.");
            }

            return SocialUserInfo.builder()
                    .socialId(String.valueOf(kakaoUserInfo.getId()))
                    .email(kakaoUserInfo.getKakaoAccount() != null ? kakaoUserInfo.getKakaoAccount().getEmail() : null)
                    .nickname(kakaoUserInfo.getProperties() != null ? kakaoUserInfo.getProperties().getNickname() : null)
                    .profileImage(kakaoUserInfo.getProperties() != null ? kakaoUserInfo.getProperties().getProfileImage() : null)
                    .provider(SocialProvider.KAKAO)
                    .build();
                    
        } catch (Exception e) {
            log.error("카카오 사용자 정보 조회 실패: {}", e.getMessage());
            throw new RuntimeException("카카오 사용자 정보 조회에 실패했습니다.");
        }
    }

    /**
     * 애플 사용자 정보 조회 (클라이언트에서 제공받은 정보 사용)
     */
    private SocialUserInfo getAppleUserInfo(SocialLoginRequest request) {
        // 애플의 경우 클라이언트에서 JWT 토큰을 파싱하여 정보를 제공
        // 실제 구현에서는 애플 JWT 토큰 검증이 필요
        return SocialUserInfo.builder()
                .socialId(request.getSocialId())
                .email(request.getEmail())
                .nickname(request.getNickname())
                .profileImage(request.getProfileImage())
                .provider(SocialProvider.APPLE)
                .build();
    }

    /**
     * 기존 사용자 확인 또는 신규 사용자 생성
     */
    private Auth findOrCreateUser(SocialUserInfo socialUserInfo, SocialProvider provider) {
        // 기존 소셜 계정 확인
        Optional<Auth> existingAuth = authRepository.findBySocialIdAndProvider(
                socialUserInfo.getSocialId(), provider);
        
        if (existingAuth.isPresent()) {
            // 기존 사용자 로그인
            log.info("기존 소셜 사용자 로그인 - 제공자: {}, 소셜ID: {}", 
                    provider, socialUserInfo.getSocialId());
            return existingAuth.get();
        }

        // 이메일로 기존 사용자 확인 (다른 소셜 계정과 연결)
        if (socialUserInfo.getEmail() != null) {
            Optional<Auth> emailAuth = authRepository.findByEmail(socialUserInfo.getEmail());
            if (emailAuth.isPresent()) {
                // 기존 사용자에 새로운 소셜 계정 연결
                return linkSocialAccount(emailAuth.get().getUser(), socialUserInfo, provider);
            }
        }

        // 신규 사용자 생성
        return createNewUser(socialUserInfo, provider);
    }

    /**
     * 기존 사용자에 소셜 계정 연결
     */
    private Auth linkSocialAccount(User existingUser, SocialUserInfo socialUserInfo, SocialProvider provider) {
        Auth newAuth = Auth.createSocialAuth(
            existingUser, 
            provider, 
            socialUserInfo.getSocialId(),
            socialUserInfo.getEmail(),
            socialUserInfo.getNickname(),
            generateUsername(socialUserInfo, provider)
        );
        
        authRepository.save(newAuth);
        
        log.info("기존 사용자에 소셜 계정 연결 - 사용자ID: {}, 제공자: {}", 
                existingUser.getUserId(), provider);
        
        return newAuth;
    }

    /**
     * 신규 사용자 생성
     */
    private Auth createNewUser(SocialUserInfo socialUserInfo, SocialProvider provider) {
        // 사용자 타입 결정 (소셜 로그인은 EMPLOYEE 또는 MEMBER)
        UserType userType = determineUserType(socialUserInfo);
        
        // User 엔티티 생성
        User newUser = User.createSocialUser(
            userType, 
            socialUserInfo.getNickname() != null ? socialUserInfo.getNickname() : "사용자",
            socialUserInfo.getProfileImage()
        );
        
        User savedUser = userRepository.save(newUser);
        
        // Auth 엔티티 생성
        Auth newAuth = Auth.createSocialAuth(
            savedUser,
            provider,
            socialUserInfo.getSocialId(),
            socialUserInfo.getEmail(),
            socialUserInfo.getNickname(),
            generateUsername(socialUserInfo, provider)
        );
        
        authRepository.save(newAuth);
        
        log.info("신규 소셜 사용자 생성 - 제공자: {}, 타입: {}, 사용자명: {}", 
                provider, userType, newAuth.getUsername());
        
        return newAuth;
    }

    /**
     * 사용자 타입 결정 로직
     * 기본적으로 MEMBER로 설정하고, 추후 관리자가 EMPLOYEE로 승격 가능
     */
    private UserType determineUserType(SocialUserInfo socialUserInfo) {
        // 테스트용: 이메일에 "owner"가 포함되면 OWNER로 설정
        if (socialUserInfo.getEmail() != null && socialUserInfo.getEmail().contains("owner")) {
            return UserType.OWNER;
        }
        
        // 테스트용: 이메일에 "employee"가 포함되면 EMPLOYEE로 설정
        if (socialUserInfo.getEmail() != null && socialUserInfo.getEmail().contains("employee")) {
            return UserType.EMPLOYEE;
        }
        
        // 기본값은 MEMBER
        return UserType.MEMBER;
    }

    /**
     * 사용자명 생성
     */
    private String generateUsername(SocialUserInfo socialUserInfo, SocialProvider provider) {
        String baseUsername = provider.name().toLowerCase() + "_" + socialUserInfo.getSocialId();
        
        // 중복 확인 및 유니크한 사용자명 생성
        String username = baseUsername;
        int counter = 1;
        while (authRepository.existsByUsername(username)) {
            username = baseUsername + "_" + counter++;
        }
        
        return username;
    }

    /**
     * Access Token 생성
     */
    private String generateAccessToken(Auth auth) {
        // CustomUserPrincipal을 기반으로 Authentication 객체 생성
        CustomUserPrincipal principal = CustomUserPrincipal.builder()
                .userId(auth.getUser().getUserId())
                .username(auth.getUsername())
                .password(auth.getPassword())
                .userType(auth.getUser().getUserType())
                .authorities(getAuthorities(auth.getUser()))
                .enabled(true)
                .build();
        
        // UsernamePasswordAuthenticationToken을 통해 Authentication 생성
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
            
        return jwtUtil.generateAccessToken(authentication);
    }

    /**
     * 사용자 권한 설정 (각 타입별로 해당 권한만 부여)
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
        
        return authorities;
    }

    /**
     * 소셜 사용자 정보 내부 DTO
     */
    @lombok.Builder
    @lombok.Getter
    private static class SocialUserInfo {
        private String socialId;
        private String email;
        private String nickname;
        private String profileImage;
        private SocialProvider provider;
    }
}