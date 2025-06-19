package com.gymmanager.config;

import com.gymmanager.config.tenant.TenantInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 웹 설정 클래스
 * 
 * CORS, 인터셉터 등 웹 관련 설정을 담당합니다.
 * Multi-tenant 인터셉터를 등록하여 모든 요청에서 사업장 컨텍스트를 관리합니다.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TenantInterceptor tenantInterceptor;

    /**
     * 인터셉터 등록
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantInterceptor)
                .addPathPatterns("/api/**")  // 모든 API 경로에 적용
                .excludePathPatterns(
                        "/api/health",              // 헬스체크
                        "/api/actuator/**",         // Actuator 엔드포인트
                        "/api/swagger-ui/**",       // Swagger UI
                        "/api/v3/api-docs/**",      // Swagger API 문서
                        "/api/error"                // 에러 페이지
                );
    }

    /**
     * CORS 설정 (React 프론트엔드 분리형)
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                // React 개발 서버 및 프로덕션 도메인 허용
                .allowedOriginPatterns(
                    "http://localhost:3000",          // React 개발 서버
                    "https://*.gymmanager.com",       // 프로덕션 도메인
                    "https://admin.gymmanager.com"    // 관리자 웹사이트
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders(
                    "X-Business-Id", 
                    "Authorization", 
                    "X-Total-Count",      // 페이징 정보
                    "X-Request-Id"        // 요청 추적용
                )
                .allowCredentials(true)
                .maxAge(3600);
    }
}