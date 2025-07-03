package com.fitness;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * 기본 애플리케이션 통합 테스트
 * H2 메모리 DB를 사용하여 전체 컨텍스트 로딩 검증
 */
@SpringBootTest
@ActiveProfiles("test")
class FitnessManagementApplicationTests {

    @Test
    void contextLoads() {
        // 애플리케이션 컨텍스트가 정상적으로 로드되는지 확인
        // H2 메모리 DB와 JPA 엔티티 스캔이 정상 작동하는지 검증
    }
}