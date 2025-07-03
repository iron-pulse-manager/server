package com.fitness.domain.user.entity;

import com.fitness.common.enums.UserStatus;
import com.fitness.common.enums.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * User 엔티티 JPA 테스트
 * H2 메모리 DB를 사용하여 엔티티 저장/조회 검증
 */
@DataJpaTest
@ActiveProfiles("test")
class UserEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void 사용자_엔티티_저장_및_조회_테스트() {
        // Given
        User user = new User();
        user.setUserType(UserType.OWNER);
        user.setName("테스트 사용자");
        user.setStatus(UserStatus.ACTIVE);
        user.setPhoneNumber("010-1234-5678");

        // When
        User savedUser = entityManager.persistAndFlush(user);

        // Then
        assertThat(savedUser.getUserId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("테스트 사용자");
        assertThat(savedUser.getUserType()).isEqualTo(UserType.OWNER);
        assertThat(savedUser.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(savedUser.getPhoneNumber()).isEqualTo("010-1234-5678");
    }

    @Test
    void 사용자_엔티티_기본값_테스트() {
        // Given
        User user = new User();
        user.setUserType(UserType.EMPLOYEE);
        user.setName("기본값 테스트");

        // When
        User savedUser = entityManager.persistAndFlush(user);

        // Then
        assertThat(savedUser.getStatus()).isEqualTo(UserStatus.ACTIVE); // 기본값 확인
        assertThat(savedUser.getCreatedAt()).isNotNull(); // BaseEntity 상속 확인
        assertThat(savedUser.getUpdatedAt()).isNotNull();
    }
}