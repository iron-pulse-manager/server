package com.fitness.domain.auth.service;

import com.fitness.domain.auth.dto.LoginRequest;
import com.fitness.domain.auth.dto.LoginResponse;
import com.fitness.common.jwt.JwtUtil;
import com.fitness.common.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 관련 비즈니스 로직을 처리하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    /**
     * 로그인 처리
     * @param request 로그인 요청 정보
     * @return 로그인 응답 (토큰 포함)
     */
    public LoginResponse login(LoginRequest request) {
        try {
            // 사용자 인증
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );

            // 토큰 생성
            String accessToken = jwtUtil.generateAccessToken(authentication);
            String refreshToken = jwtUtil.generateRefreshToken(authentication.getName());

            // 사용자 정보 추출
            CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();

            log.info("로그인 성공 - 사용자: {}, 타입: {}", 
                    userPrincipal.getUsername(), userPrincipal.getUserType());

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .userId(userPrincipal.getUserId())
                    .username(userPrincipal.getUsername())
                    .userType(userPrincipal.getUserType())
                    .expiresIn(900L) // 15분
                    .build();

        } catch (AuthenticationException e) {
            log.error("로그인 실패 - 사용자: {}, 원인: {}", request.getUsername(), e.getMessage());
            throw new RuntimeException("로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.");
        }
    }

    /**
     * 토큰 갱신
     * @param refreshToken Refresh Token
     * @return 새로운 토큰
     */
    public LoginResponse refreshToken(String refreshToken) {
        try {
            if (!jwtUtil.validateToken(refreshToken)) {
                throw new RuntimeException("유효하지 않은 Refresh Token입니다.");
            }

            String username = jwtUtil.getUsernameFromToken(refreshToken);
            String newAccessToken = jwtUtil.refreshToken(refreshToken);

            log.info("토큰 갱신 성공 - 사용자: {}", username);

            return LoginResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken) // 기존 Refresh Token 유지
                    .tokenType("Bearer")
                    .username(username)
                    .expiresIn(900L)
                    .build();

        } catch (Exception e) {
            log.error("토큰 갱신 실패: {}", e.getMessage());
            throw new RuntimeException("토큰 갱신에 실패했습니다. 다시 로그인해주세요.");
        }
    }

    /**
     * 로그아웃 처리
     * @param username 사용자명
     */
    public void logout(String username) {
        // TODO: 토큰 블랙리스트 처리 (Redis 등을 사용하여 구현)
        log.info("로그아웃 처리 - 사용자: {}", username);
    }

    /**
     * 현재 사용자 정보 조회
     * @param authentication 인증 정보
     * @return 사용자 정보
     */
    @Transactional(readOnly = true)
    public Object getCurrentUserInfo(Authentication authentication) {
        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        
        return LoginResponse.builder()
                .userId(userPrincipal.getUserId())
                .username(userPrincipal.getUsername())
                .userType(userPrincipal.getUserType())
                .tokenType("Bearer")
                .build();
    }
}