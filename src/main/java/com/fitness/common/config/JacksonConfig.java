package com.fitness.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Jackson 설정 클래스
 * - JSON 직렬화/역직렬화 설정
 * - 날짜 시간 형식 설정
 * - 프로퍼티 네이밍 전략 설정
 */
@Configuration
public class JacksonConfig {

    /**
     * ObjectMapper 빈 등록
     * - 전역적으로 사용할 ObjectMapper 설정
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // Java 8 시간 API 모듈 등록
        mapper.registerModule(new JavaTimeModule());
        
        // 날짜를 타임스탬프로 직렬화하지 않도록 설정
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // 프로퍼티 네이밍 전략 설정 (snake_case)
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        
        // 빈 객체 직렬화 허용
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        
        return mapper;
    }
}