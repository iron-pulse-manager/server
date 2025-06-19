package com.gymmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 헬스장 관리 SaaS 메인 애플리케이션 클래스
 * 
 * Multi-tenant 아키텍처를 지원하는 Spring Boot 애플리케이션입니다.
 * 각 사업장(Business)별로 독립적인 데이터 접근을 제공합니다.
 * 
 * @author GymManager Team
 * @version 1.0
 * @since 2024
 */
@SpringBootApplication
@EnableJpaAuditing                  // JPA Auditing 활성화 (생성일, 수정일 자동 관리)
@EnableCaching                      // 캐싱 활성화 (Multi-tenant별 캐시 지원)
@EnableAsync                        // 비동기 처리 활성화 (SMS 발송 등)
@EnableTransactionManagement        // 트랜잭션 관리 활성화
public class GymManagerServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymManagerServerApplication.class, args);
    }
}