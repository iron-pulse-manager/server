package com.fitness.common.config;

import com.fitness.common.jwt.JwtAccessDeniedHandler;
import com.fitness.common.jwt.JwtAuthenticationEntryPoint;
import com.fitness.common.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security 설정 클래스
 * - JWT 기반 인증 및 권한 부여 설정
 * - CORS 설정
 * - 보안 필터 체인 설정
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 패스워드 인코더 빈 등록
     * BCrypt 해시 알고리즘을 사용하여 패스워드 암호화
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager 빈 등록
     * Spring Security의 인증 매니저
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * 보안 필터 체인 설정
     * - JWT 기반 인증 설정
     * - CORS 설정
     * - 세션 관리 설정
     * - 접근 권한 설정
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 비활성화 (JWT 사용으로 인해 불필요)
            .csrf(AbstractHttpConfigurer::disable)
            
            // CORS 설정
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 세션 관리 정책 설정 (JWT 사용으로 STATELESS)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 예외 처리 설정
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
            )
            
            // 요청 권한 설정
            .authorizeHttpRequests(auth -> auth
                // 인증 관련 API는 모든 사용자 접근 가능
                .requestMatchers("/api/auth/**").permitAll()
                
                // 공개 API는 모든 사용자 접근 가능
                .requestMatchers("/api/public/**").permitAll()
                
                // 테스트 공개 API는 모든 사용자 접근 가능
                .requestMatchers("/api/test/public").permitAll()
                
                // Swagger UI 관련 경로는 모든 사용자 접근 가능
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                
                // 액추에이터 관련 경로는 모든 사용자 접근 가능 (운영 환경에서는 제한 필요)
                .requestMatchers("/actuator/**").permitAll()
                
                // 정적 리소스는 모든 사용자 접근 가능
                .requestMatchers("/static/**", "/public/**", "/favicon.ico").permitAll()
                
                // 회원용 API는 MEMBER 권한 필요
                .requestMatchers("/api/member/**").hasRole("MEMBER")
                
                // 직원용 API는 EMPLOYEE 권한 필요
                .requestMatchers("/api/employee/**").hasRole("EMPLOYEE")
                
                // 사장님용 API는 OWNER 권한 필요
                .requestMatchers("/api/owner/**").hasRole("OWNER")
                
                // 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
            )
            
            // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS 설정
     * - 허용할 오리진, 메소드, 헤더 등을 설정
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 허용할 오리진 설정 (개발 환경)
        configuration.setAllowedOriginPatterns(List.of("*"));
        
        // 허용할 HTTP 메서드 설정
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        
        // 허용할 헤더 설정
        configuration.setAllowedHeaders(List.of("*"));
        
        // 인증 정보 포함 허용
        configuration.setAllowCredentials(true);
        
        // 응답 헤더 노출 설정
        configuration.setExposedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        
        // 프리플라이트 요청 캐시 시간 설정
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}