package com.fitness;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * 전체 애플리케이션 컨텍스트 로딩 테스트
 * 엔티티 관계 매핑 수정 후 검증
 */
@SpringBootTest
@ActiveProfiles("test")
class ApplicationContextTest {

    @Test
    void 애플리케이션_컨텍스트_로딩_테스트() {
        // 모든 엔티티가 정상적으로 로드되고 
        // Hibernate 매핑이 성공하는지 확인
        System.out.println("✅ 애플리케이션 컨텍스트 로딩 성공!");
    }
}