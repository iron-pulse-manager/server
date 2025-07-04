package com.fitness.domain.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * JWT 인증 및 권한 테스트용 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/test")
@Tag(name = "테스트 API", description = "JWT 인증 및 권한 테스트용 API")
public class TestController {

    /**
     * 공개 API - 인증 불필요
     */
    @GetMapping("/public")
    @Operation(summary = "공개 API", description = "인증이 필요하지 않은 공개 API입니다.")
    public ResponseEntity<Map<String, Object>> publicEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "공개 API 접근 성공!");
        response.put("timestamp", System.currentTimeMillis());
        response.put("requiresAuth", false);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 인증 필요 API
     */
    @GetMapping("/authenticated")
    @Operation(summary = "인증 필요 API", description = "유효한 JWT 토큰이 필요한 API입니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Map<String, Object>> authenticatedEndpoint(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "인증된 사용자 접근 성공!");
        response.put("username", authentication.getName());
        response.put("authorities", authentication.getAuthorities());
        response.put("timestamp", System.currentTimeMillis());
        
        log.info("인증된 사용자 접근: {}", authentication.getName());
        
        return ResponseEntity.ok(response);
    }

    /**
     * OWNER 권한 필요 API
     */
    @GetMapping("/owner")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "사장님 전용 API", description = "OWNER 권한이 필요한 API입니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Map<String, Object>> ownerEndpoint(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "사장님 접근 성공!");
        response.put("username", authentication.getName());
        response.put("role", "OWNER");
        response.put("timestamp", System.currentTimeMillis());
        
        log.info("사장님 접근: {}", authentication.getName());
        
        return ResponseEntity.ok(response);
    }

    /**
     * EMPLOYEE 권한 필요 API
     */
    @GetMapping("/employee")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @Operation(summary = "직원 전용 API", description = "EMPLOYEE 권한이 필요한 API입니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Map<String, Object>> employeeEndpoint(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "직원 접근 성공!");
        response.put("username", authentication.getName());
        response.put("role", "EMPLOYEE");
        response.put("timestamp", System.currentTimeMillis());
        
        log.info("직원 접근: {}", authentication.getName());
        
        return ResponseEntity.ok(response);
    }

    /**
     * MEMBER 권한 필요 API
     */
    @GetMapping("/member")
    @PreAuthorize("hasRole('MEMBER')")
    @Operation(summary = "회원 전용 API", description = "MEMBER 권한이 필요한 API입니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Map<String, Object>> memberEndpoint(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "회원 접근 성공!");
        response.put("username", authentication.getName());
        response.put("role", "MEMBER");
        response.put("timestamp", System.currentTimeMillis());
        
        log.info("회원 접근: {}", authentication.getName());
        
        return ResponseEntity.ok(response);
    }

    /**
     * JWT 토큰 정보 확인 API
     */
    @GetMapping("/token-info")
    @Operation(summary = "토큰 정보 확인", description = "현재 JWT 토큰의 정보를 확인합니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Map<String, Object>> tokenInfo(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("principal", authentication.getPrincipal().toString());
        response.put("authorities", authentication.getAuthorities());
        response.put("details", authentication.getDetails());
        response.put("authenticated", authentication.isAuthenticated());
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
}