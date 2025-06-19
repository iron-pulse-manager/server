package com.gymmanager.api.common.controller;

import com.gymmanager.common.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 공통 API 컨트롤러
 * 
 * 모든 클라이언트에서 공통으로 사용하는 API를 제공합니다.
 * 인증, 파일 업로드, 헬스체크 등의 기능을 포함합니다.
 */
@RestController
@RequestMapping("/api/v1/common")
public class CommonController {

    /**
     * 서버 헬스체크
     */
    @GetMapping("/health")
    public ApiResponse<Map<String, Object>> health() {
        Map<String, Object> healthInfo = Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now(),
            "service", "gym-manager-server",
            "version", "1.0.0"
        );
        
        return ApiResponse.success("서버가 정상적으로 동작 중입니다.", healthInfo);
    }

    /**
     * API 버전 정보
     */
    @GetMapping("/version")
    public ApiResponse<Map<String, Object>> version() {
        Map<String, Object> versionInfo = Map.of(
            "api_version", "v1",
            "server_version", "1.0.0",
            "supported_clients", Map.of(
                "web", "1.0.0",
                "staff_app", "1.0.0", 
                "member_app", "1.0.0"
            ),
            "build_time", "2024-01-01T00:00:00"
        );
        
        return ApiResponse.success(versionInfo);
    }
}