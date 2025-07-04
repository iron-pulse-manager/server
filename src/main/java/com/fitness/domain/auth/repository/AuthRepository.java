package com.fitness.domain.auth.repository;

import com.fitness.domain.auth.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Auth 엔티티에 대한 데이터 접근 레포지토리
 */
@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {

    /**
     * 사용자명으로 인증 정보 조회 (User 정보 포함)
     * @param username 사용자명
     * @return Auth 엔티티 (User와 함께 로드)
     */
    @Query("SELECT a FROM Auth a JOIN FETCH a.user WHERE a.username = :username")
    Optional<Auth> findByUsername(@Param("username") String username);

    /**
     * 이메일로 인증 정보 조회
     * @param email 이메일
     * @return Auth 엔티티
     */
    Optional<Auth> findByEmail(String email);

    /**
     * 소셜 ID와 프로바이더로 인증 정보 조회
     * @param socialId 소셜 ID
     * @param provider 소셜 프로바이더
     * @return Auth 엔티티
     */
    @Query("SELECT a FROM Auth a JOIN FETCH a.user WHERE a.socialId = :socialId AND a.provider = :provider")
    Optional<Auth> findBySocialIdAndProvider(@Param("socialId") String socialId, 
                                           @Param("provider") com.fitness.common.enums.SocialProvider provider);

    /**
     * 사용자명 중복 확인
     * @param username 사용자명
     * @return 존재 여부
     */
    boolean existsByUsername(String username);

    /**
     * 이메일 중복 확인
     * @param email 이메일
     * @return 존재 여부
     */
    boolean existsByEmail(String email);
}