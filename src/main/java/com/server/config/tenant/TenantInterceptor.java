package com.gymmanager.config.tenant;

import com.gymmanager.common.exception.TenantException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Multi-tenant 인터셉터
 * 
 * HTTP 요청에서 사업장 정보를 추출하여 TenantContext에 설정합니다.
 * 요청 처리 완료 후 컨텍스트를 정리합니다.
 * 
 * 사업장 정보는 다음 순서로 확인합니다:
 * 1. HTTP 헤더 (X-Business-Id)
 * 2. 요청 파라미터 (businessId)
 * 3. JWT 토큰에서 추출 (향후 구현)
 */
@Component
@Slf4j
public class TenantInterceptor implements HandlerInterceptor {

    private static final String BUSINESS_ID_HEADER = "X-Business-Id";
    private static final String BUSINESS_ID_PARAM = "businessId";

    /**
     * 요청 처리 전에 사업장 컨텍스트를 설정합니다.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            // 사업장 ID 추출
            Long businessId = extractBusinessId(request);
            
            if (businessId != null) {
                TenantContext.setCurrentBusinessId(businessId);
                log.debug("Tenant context set for business ID: {} for URI: {}", 
                        businessId, request.getRequestURI());
            } else {
                // 특정 API는 사업장 정보 없이도 접근 가능 (로그인 등)
                if (requiresBusinessContext(request)) {
                    log.warn("Business ID required but not found for URI: {}", request.getRequestURI());
                    throw TenantException.contextNotSet();
                }
            }
            
            return true;
            
        } catch (Exception e) {
            log.error("Failed to set tenant context for URI: {}, Error: {}", 
                    request.getRequestURI(), e.getMessage());
            throw e;
        }
    }

    /**
     * 요청 처리 완료 후 컨텍스트를 정리합니다.
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                               Object handler, Exception ex) {
        try {
            TenantContext.clear();
            log.debug("Tenant context cleared for URI: {}", request.getRequestURI());
        } catch (Exception e) {
            log.warn("Failed to clear tenant context: {}", e.getMessage());
        }
    }

    /**
     * HTTP 요청에서 사업장 ID를 추출합니다.
     */
    private Long extractBusinessId(HttpServletRequest request) {
        // 1. HTTP 헤더에서 확인
        String businessIdHeader = request.getHeader(BUSINESS_ID_HEADER);
        if (StringUtils.hasText(businessIdHeader)) {
            try {
                return Long.parseLong(businessIdHeader);
            } catch (NumberFormatException e) {
                log.warn("Invalid business ID in header: {}", businessIdHeader);
            }
        }

        // 2. 요청 파라미터에서 확인
        String businessIdParam = request.getParameter(BUSINESS_ID_PARAM);
        if (StringUtils.hasText(businessIdParam)) {
            try {
                return Long.parseLong(businessIdParam);
            } catch (NumberFormatException e) {
                log.warn("Invalid business ID in parameter: {}", businessIdParam);
            }
        }

        // 3. JWT 토큰에서 추출 (향후 구현)
        // TODO: JWT 토큰에서 사업장 ID 추출 로직 추가

        return null;
    }

    /**
     * 해당 요청이 사업장 컨텍스트를 필요로 하는지 확인합니다.
     */
    private boolean requiresBusinessContext(HttpServletRequest request) {
        String uri = request.getRequestURI();
        
        // 사업장 컨텍스트가 필요하지 않은 API들
        String[] excludedPaths = {
            "/api/auth/login",
            "/api/auth/refresh",
            "/api/health",
            "/api/actuator",
            "/api/swagger",
            "/api/v3/api-docs"
        };
        
        for (String excludedPath : excludedPaths) {
            if (uri.startsWith(excludedPath)) {
                return false;
            }
        }
        
        // 그 외의 모든 API는 사업장 컨텍스트 필요
        return true;
    }
}