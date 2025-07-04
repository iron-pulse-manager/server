package com.fitness.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 설정 프로퍼티 클래스
 * application.yml의 jwt 설정을 바인딩하여 사용
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    
    /**
     * JWT 서명에 사용할 비밀 키
     */
    private String secret;
    
    /**
     * Access Token 만료 시간 (밀리초)
     * 기본값: 15분 (900,000ms)
     */
    private Long accessTokenExpiration;
    
    /**
     * Refresh Token 만료 시간 (밀리초)
     * 기본값: 7일 (604,800,000ms)
     */
    private Long refreshTokenExpiration;
}