package com.fitness.domain.user.entity;

import com.fitness.common.enums.UserStatus;
import com.fitness.common.enums.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * User 엔티티만 테스트 (다른 엔티티 스캔 제외)
 * H2 메모리 DB를 사용하여 기본 CRUD 검증
 */
@DataJpaTest
@EntityScan(basePackages = {"com.fitness.common", "com.fitness.domain.user.entity", "com.fitness.domain.auth.entity"})
@ActiveProfiles("test")
class SimpleUserTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void User_엔티티_기본_저장_테스트() {
        // Given
        User user = new User();
        user.setUserType(UserType.OWNER);
        user.setName("테스트 사용자");
        user.setStatus(UserStatus.ACTIVE);

        // When
        User savedUser = entityManager.persistAndFlush(user);

        // Then
        assertThat(savedUser.getUserId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("테스트 사용자");
        assertThat(savedUser.getUserType()).isEqualTo(UserType.OWNER);
        assertThat(savedUser.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
}