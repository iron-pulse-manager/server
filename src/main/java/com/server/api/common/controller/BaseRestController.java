package com.gymmanager.api.common.controller;

import com.gymmanager.common.dto.ApiResponse;
import com.gymmanager.config.security.AuthenticationContext;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

/**
 * REST API 컨트롤러 기본 클래스
 * 
 * 모든 REST API 컨트롤러의 공통 기능을 제공합니다.
 * - 요청 추적 ID 생성
 * - API 버전 관리
 * - 클라이언트 인증 정보 접근
 * - 공통 응답 형식 제공
 */
public abstract class BaseRestController {

    @Autowired
    private HttpServletRequest request;

    /**
     * 현재 API 버전
     */
    protected static final String API_VERSION = "v1";

    /**
     * 성공 응답 생성 (데이터 포함)
     */
    protected <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("요청이 성공적으로 처리되었습니다.")
                .data(data)
                .requestId(getRequestId())
                .apiVersion(API_VERSION)
                .build();
    }

    /**
     * 성공 응답 생성 (메시지만)
     */
    protected <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .requestId(getRequestId())
                .apiVersion(API_VERSION)
                .build();
    }

    /**
     * 성공 응답 생성 (데이터와 메시지)
     */
    protected <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .requestId(getRequestId())
                .apiVersion(API_VERSION)
                .build();
    }

    /**
     * 실패 응답 생성
     */
    protected <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .requestId(getRequestId())
                .apiVersion(API_VERSION)
                .build();
    }

    /**
     * 실패 응답 생성 (에러 코드 포함)
     */
    protected <T> ApiResponse<T> error(String message, String errorCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .requestId(getRequestId())
                .apiVersion(API_VERSION)
                .build();
    }

    /**
     * 현재 요청의 추적 ID 조회
     */
    protected String getRequestId() {
        return MDC.get("requestId");
    }

    /**
     * 현재 인증된 사용자 ID 조회
     */
    protected Long getCurrentUserId() {
        return AuthenticationContext.getCurrentUserId();
    }

    /**
     * 현재 사업장 ID 조회
     */
    protected Long getCurrentBusinessId() {
        return AuthenticationContext.getCurrentBusinessId();
    }

    /**
     * 현재 인증 컨텍스트 조회
     */
    protected AuthenticationContext getCurrentAuth() {
        return AuthenticationContext.getCurrent();
    }

    /**
     * 클라이언트 IP 주소 조회
     */
    protected String getClientIpAddress() {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    /**
     * User-Agent 조회
     */
    protected String getUserAgent() {
        return request.getHeader("User-Agent");
    }

    /**
     * 요청 URI 조회
     */
    protected String getRequestUri() {
        return request.getRequestURI();
    }

    /**
     * HTTP 메서드 조회
     */
    protected String getHttpMethod() {
        return request.getMethod();
    }
}