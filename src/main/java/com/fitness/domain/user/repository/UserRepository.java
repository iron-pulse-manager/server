package com.fitness.domain.member.repository;

import com.fitness.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 회원 리포지토리
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 전화번호로 회원 조회
     */
    Optional<Member> findByPhone(String phone);

    /**
     * 이메일로 회원 조회
     */
    Optional<Member> findByEmail(String email);

    /**
     * 이름으로 회원 검색 (부분 일치)
     */
    List<Member> findByNameContaining(String name);

    /**
     * 상태별 회원 조회
     */
    List<Member> findByStatus(Member.MemberStatus status);

    /**
     * 활성 회원 수 조회
     */
    @Query("SELECT COUNT(m) FROM Member m WHERE m.status = :status")
    long countByStatus(@Param("status") Member.MemberStatus status);

    /**
     * 전화번호 또는 이름으로 회원 검색
     */
    @Query("SELECT m FROM Member m WHERE m.phone LIKE %:keyword% OR m.name LIKE %:keyword%")
    List<Member> findByPhoneContainingOrNameContaining(@Param("keyword") String keyword);
}