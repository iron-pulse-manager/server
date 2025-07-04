package com.fitness.domain.auth.controller;

import com.fitness.domain.auth.dto.LoginRequest;
import com.fitness.domain.auth.dto.LoginResponse;
import com.fitness.domain.auth.dto.SocialLoginRequest;
import com.fitness.domain.auth.service.AuthService;
import com.fitness.domain.auth.service.SocialLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 인증 관련 API 컨트롤러
 * - 로그인/로그아웃
 * - 토큰 갱신
 * - 인증 상태 확인
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증 API", description = "로그인, 로그아웃, 토큰 관리 관련 API")
public class AuthController {

    private final AuthService authService;
    private final SocialLoginService socialLoginService;

    /**
     * 일반 로그인 (OWNER 전용 - 웹)
     */
    @PostMapping("/login")
    @Operation(summary = "일반 로그인", description = "OWNER 사용자의 ID/PW 로그인을 처리하고 JWT 토큰을 발급합니다.")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("일반 로그인 시도: {}", request.getUsername());
        
        LoginResponse response = authService.login(request);
        
        log.info("일반 로그인 성공: {}", request.getUsername());
        return ResponseEntity.ok(response);
    }

    /**
     * 소셜 로그인 (EMPLOYEE, MEMBER 전용 - 앱)
     */
    @PostMapping("/social-login")
    @Operation(summary = "소셜 로그인", description = "카카오/애플 소셜 로그인을 처리하고 JWT 토큰을 발급합니다.")
    public ResponseEntity<LoginResponse> socialLogin(@Valid @RequestBody SocialLoginRequest request) {
        log.info("소셜 로그인 시도: {}", request.getProvider());
        
        LoginResponse response = socialLoginService.socialLogin(request);
        
        log.info("소셜 로그인 성공: {} - {}", request.getProvider(), response.getUsername());
        return ResponseEntity.ok(response);
    }

    /**
     * 카카오 로그인
     */
    @PostMapping("/kakao")
    @Operation(summary = "카카오 로그인", description = "카카오 소셜 로그인을 처리합니다.")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestParam String accessToken) {
        log.info("카카오 로그인 시도");
        
        SocialLoginRequest request = new SocialLoginRequest();
        request.setProvider(com.fitness.common.enums.SocialProvider.KAKAO);
        request.setAccessToken(accessToken);
        
        LoginResponse response = socialLoginService.socialLogin(request);
        
        log.info("카카오 로그인 성공: {}", response.getUsername());
        return ResponseEntity.ok(response);
    }

    /**
     * 애플 로그인
     */
    @PostMapping("/apple")
    @Operation(summary = "애플 로그인", description = "애플 소셜 로그인을 처리합니다.")
    public ResponseEntity<LoginResponse> appleLogin(
            @RequestParam String accessToken,
            @RequestParam String socialId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String nickname) {
        log.info("애플 로그인 시도");
        
        SocialLoginRequest request = new SocialLoginRequest();
        request.setProvider(com.fitness.common.enums.SocialProvider.APPLE);
        request.setAccessToken(accessToken);
        request.setSocialId(socialId);
        request.setEmail(email);
        request.setNickname(nickname);
        
        LoginResponse response = socialLoginService.socialLogin(request);
        
        log.info("애플 로그인 성공: {}", response.getUsername());
        return ResponseEntity.ok(response);
    }

    /**
     * 토큰 갱신
     */
    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신", description = "Refresh Token을 사용하여 새로운 Access Token을 발급합니다.")
    public ResponseEntity<LoginResponse> refresh(@RequestHeader("Authorization") String refreshToken) {
        log.info("토큰 갱신 요청");
        
        String token = refreshToken.substring(7); // "Bearer " 제거
        LoginResponse response = authService.refreshToken(token);
        
        log.info("토큰 갱신 성공");
        return ResponseEntity.ok(response);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "사용자 로그아웃을 처리합니다.")
    public ResponseEntity<Void> logout(Authentication authentication) {
        log.info("로그아웃: {}", authentication.getName());
        
        authService.logout(authentication.getName());
        
        return ResponseEntity.ok().build();
    }

    /**
     * 인증 상태 확인
     */
    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    public ResponseEntity<Object> getCurrentUser(Authentication authentication) {
        log.info("사용자 정보 조회: {}", authentication.getName());
        
        Object userInfo = authService.getCurrentUserInfo(authentication);
        
        return ResponseEntity.ok(userInfo);
    }

    /**
     * 인증 테스트 (개발용)
     */
    @GetMapping("/test")
    @Operation(summary = "인증 테스트", description = "JWT 인증이 정상 작동하는지 테스트합니다.")
    public ResponseEntity<String> authTest(Authentication authentication) {
        return ResponseEntity.ok("인증 성공! 사용자: " + authentication.getName());
    }
}