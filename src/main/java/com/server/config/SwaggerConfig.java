package com.gymmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI 설정 클래스
 * 
 * REST API 문서화를 위한 Swagger UI 설정입니다.
 * 웹 개발자와 앱 개발자가 API 스펙을 쉽게 확인할 수 있습니다.
 */
@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(serverList())
                .addSecurityItem(securityRequirement())
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", securityScheme()));
    }

    /**
     * API 기본 정보
     */
    private Info apiInfo() {
        return new Info()
                .title("헬스장 관리 SaaS REST API")
                .description("""
                        ## 헬스장 관리 SaaS API 문서
                        
                        이 API는 헬스장, 필라테스 등 피트니스 사업장 관리를 위한 REST API입니다.
                        
                        ### 지원 클라이언트
                        - **웹 관리자**: React 기반 관리자 웹사이트 (`/api/v1/web/`)
                        - **직원 앱**: Flutter 기반 직원용 모바일 앱 (`/api/v1/staff/`)
                        - **회원 앱**: Flutter 기반 회원용 모바일 앱 (`/api/v1/member/`)
                        
                        ### 인증 방식
                        - **JWT Bearer Token**: 모든 보호된 API는 Authorization 헤더에 JWT 토큰 필요
                        - **Multi-tenant**: X-Business-Id 헤더로 사업장 구분
                        
                        ### 응답 형식
                        모든 API는 표준화된 응답 형식을 사용합니다:
                        ```json
                        {
                          "success": true,
                          "message": "성공 메시지",
                          "data": { ... },
                          "requestId": "abc123",
                          "apiVersion": "v1",
                          "timestamp": "2024-01-01T10:00:00"
                        }
                        ```
                        """)
                .version("v1.0.0")
                .contact(contact())
                .license(license());
    }

    /**
     * 연락처 정보
     */
    private Contact contact() {
        return new Contact()
                .name("헬스장 관리 SaaS 개발팀")
                .email("dev@gymmanager.com")
                .url("https://gymmanager.com");
    }

    /**
     * 라이센스 정보
     */
    private License license() {
        return new License()
                .name("Proprietary License")
                .url("https://gymmanager.com/license");
    }

    /**
     * 서버 목록
     */
    private List<Server> serverList() {
        return List.of(
                new Server()
                        .url("http://localhost:" + serverPort)
                        .description("로컬 개발 서버"),
                new Server()
                        .url("https://api-dev.gymmanager.com")
                        .description("개발 서버"),
                new Server()
                        .url("https://api.gymmanager.com")
                        .description("프로덕션 서버")
        );
    }

    /**
     * 보안 요구사항
     */
    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement()
                .addList("bearerAuth");
    }

    /**
     * 보안 스키마
     */
    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .description("""
                        JWT Bearer Token 인증
                        
                        **사용 방법:**
                        1. `/api/v1/common/auth/login` 엔드포인트로 로그인
                        2. 응답으로 받은 `accessToken`을 사용
                        3. Authorization 헤더에 `Bearer {token}` 형식으로 전송
                        
                        **예시:**
                        ```
                        Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
                        ```
                        """);
    }
}