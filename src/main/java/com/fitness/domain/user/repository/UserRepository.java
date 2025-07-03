package com.fitness.domain.user.repository;

import com.fitness.common.enums.UserStatus;
import com.fitness.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 리포지토리
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 전화번호로 사용자 조회
     */
    Optional<User> findByPhoneNumber(String phoneNumber);

    /**
     * 이름으로 사용자 검색 (부분 일치)
     */
    List<User> findByNameContaining(String name);

    /**
     * 상태별 사용자 조회
     */
    List<User> findByStatus(UserStatus status);

    /**
     * 활성 사용자 수 조회
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = :status")
    long countByStatus(@Param("status") UserStatus status);

    /**
     * 전화번호 또는 이름으로 사용자 검색
     */
    @Query("SELECT u FROM User u WHERE u.phoneNumber LIKE %:keyword% OR u.name LIKE %:keyword%")
    List<User> findByPhoneNumberContainingOrNameContaining(@Param("keyword") String keyword);

    /**
     * Auth ID로 사용자 조회 (1:N 관계로 변경)
     */
    @Query("SELECT u FROM User u JOIN u.authList a WHERE a.authId = :authId")
    Optional<User> findByAuthId(@Param("authId") Long authId);
}