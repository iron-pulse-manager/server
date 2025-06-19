package com.gymmanager.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * 요청 추적 필터
 * 
 * 모든 HTTP 요청에 고유한 추적 ID를 부여하고 로그에 기록합니다.
 * REST API의 요청-응답을 추적하고 디버깅할 때 유용합니다.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RequestTrackingFilter implements Filter {

    private static final String REQUEST_ID_HEADER = "X-Request-Id";
    private static final String REQUEST_ID_MDC_KEY = "requestId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        try {
            // 요청 ID 생성 또는 기존 헤더에서 추출
            String requestId = extractOrGenerateRequestId(httpRequest);
            
            // MDC에 요청 ID 설정 (로그에 자동 포함)
            MDC.put(REQUEST_ID_MDC_KEY, requestId);
            
            // 응답 헤더에 요청 ID 추가
            httpResponse.setHeader(REQUEST_ID_HEADER, requestId);
            
            // 요청 시작 로그
            long startTime = System.currentTimeMillis();
            
            log.info("REST API Request Started - Method: {}, URI: {}, IP: {}, User-Agent: {}", 
                    httpRequest.getMethod(),
                    httpRequest.getRequestURI(),
                    getClientIpAddress(httpRequest),
                    httpRequest.getHeader("User-Agent"));
            
            // 다음 필터 실행
            chain.doFilter(request, response);
            
            // 요청 완료 로그
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            log.info("REST API Request Completed - Status: {}, Duration: {}ms", 
                    httpResponse.getStatus(), duration);
            
        } finally {
            // MDC 정리
            MDC.clear();
        }
    }

    /**
     * 요청 ID 추출 또는 생성
     */
    private String extractOrGenerateRequestId(HttpServletRequest request) {
        // 클라이언트가 제공한 요청 ID가 있으면 사용
        String requestId = request.getHeader(REQUEST_ID_HEADER);
        
        if (requestId == null || requestId.trim().isEmpty()) {
            // 없으면 새로 생성
            requestId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        }
        
        return requestId;
    }

    /**
     * 클라이언트 IP 주소 추출
     */
    private String getClientIpAddress(HttpServletRequest request) {
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
}