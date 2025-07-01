package com.fitness.domain.business.repository;

import com.fitness.domain.business.entity.BusinessMember;
import com.fitness.domain.business.entity.BusinessMember.BusinessMemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 사업장-회원 소속관계 리포지토리
 */
@Repository
public interface BusinessMemberRepository extends JpaRepository<BusinessMember, Long>, BusinessMemberRepositoryCustom {

    /**
     * 특정 사업장의 특정 회원 소속 정보 조회
     */
    Optional<BusinessMember> findByBusinessIdAndMemberId(Long businessId, Long memberId);

    /**
     * 특정 사업장의 특정 상태 회원 목록 조회
     */
    List<BusinessMember> findByBusinessIdAndStatus(Long businessId, BusinessMemberStatus status);

    /**
     * 특정 사업장의 특정 상태 회원 목록 조회 (페이징)
     */
    Page<BusinessMember> findByBusinessIdAndStatus(Long businessId, BusinessMemberStatus status, Pageable pageable);

    /**
     * 특정 회원의 모든 사업장 소속 정보 조회
     */
    List<BusinessMember> findByMemberId(Long memberId);

    /**
     * 특정 회원의 현재 활성 사업장 소속 정보 조회
     */
    List<BusinessMember> findByMemberIdAndStatus(Long memberId, BusinessMemberStatus status);

    /**
     * 특정 사업장의 활성 회원 목록 조회 (Member 정보 포함)
     */
    @Query("SELECT bm FROM BusinessMember bm " +
           "JOIN FETCH bm.member m " +
           "WHERE bm.businessId = :businessId " +
           "AND bm.status = 'ACTIVE' " +
           "ORDER BY bm.joinDate DESC")
    List<BusinessMember> findActiveMembersByBusinessId(@Param("businessId") Long businessId);

    /**
     * 특정 사업장의 활성 회원 목록 조회 (페이징, Member 정보 포함)
     */
    @Query("SELECT bm FROM BusinessMember bm " +
           "JOIN FETCH bm.member m " +
           "WHERE bm.businessId = :businessId " +
           "AND bm.status = 'ACTIVE'")
    Page<BusinessMember> findActiveMembersByBusinessId(@Param("businessId") Long businessId, Pageable pageable);

    /**
     * 특정 사업장의 만료임박 회원 목록 조회
     */
    @Query("SELECT bm FROM BusinessMember bm " +
           "JOIN FETCH bm.member m " +
           "WHERE bm.businessId = :businessId " +
           "AND bm.status = 'ACTIVE' " +
           "ORDER BY bm.joinDate DESC")
    List<BusinessMember> findExpiringSoonMembersByBusinessId(@Param("businessId") Long businessId);

    /**
     * 특정 사업장의 만료된 회원 목록 조회
     */
    @Query("SELECT bm FROM BusinessMember bm " +
           "JOIN FETCH bm.member m " +
           "WHERE bm.businessId = :businessId " +
           "AND bm.status = 'EXPIRED' " +
           "ORDER BY bm.updatedAt DESC")
    List<BusinessMember> findExpiredMembersByBusinessId(@Param("businessId") Long businessId);

    /**
     * 특정 사업장의 회원 검색 (이름 또는 전화번호로 검색)
     */
    @Query("SELECT bm FROM BusinessMember bm " +
           "JOIN FETCH bm.member m " +
           "WHERE bm.businessId = :businessId " +
           "AND bm.status = :status " +
           "AND (m.name LIKE %:keyword% OR m.phone LIKE %:keyword% OR bm.memberNumber LIKE %:keyword%)")
    Page<BusinessMember> findByBusinessIdAndStatusAndMemberKeyword(
            @Param("businessId") Long businessId,
            @Param("status") BusinessMemberStatus status,
            @Param("keyword") String keyword,
            Pageable pageable);

    /**
     * 특정 트레이너의 담당 회원 목록 조회
     */
    @Query("SELECT bm FROM BusinessMember bm " +
           "JOIN FETCH bm.member m " +
           "WHERE bm.businessId = :businessId " +
           "AND bm.trainerId = :trainerId " +
           "AND bm.status = 'ACTIVE' " +
           "ORDER BY bm.joinDate DESC")
    List<BusinessMember> findActiveMembersByBusinessIdAndTrainerId(
            @Param("businessId") Long businessId, 
            @Param("trainerId") Long trainerId);

    /**
     * 특정 트레이너의 담당 회원 수 조회
     */
    Long countByBusinessIdAndTrainerIdAndStatus(Long businessId, Long trainerId, BusinessMemberStatus status);

    /**
     * 특정 기간에 가입한 회원 수 조회
     */
    @Query("SELECT COUNT(bm) FROM BusinessMember bm " +
           "WHERE bm.businessId = :businessId " +
           "AND bm.status = 'ACTIVE' " +
           "AND bm.joinDate BETWEEN :startDate AND :endDate")
    Long countNewMembersByBusinessIdAndPeriod(@Param("businessId") Long businessId,
                                             @Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);

    /**
     * 특정 사업장의 상태별 회원 수 조회
     */
    Long countByBusinessIdAndStatus(Long businessId, BusinessMemberStatus status);

    /**
     * 특정 사업장의 전체 회원 수 조회
     */
    Long countByBusinessId(Long businessId);

    /**
     * 회원이 특정 사업장에 이미 등록되어 있는지 확인
     */
    boolean existsByBusinessIdAndMemberId(Long businessId, Long memberId);

    /**
     * 특정 회원의 현재 활성 사업장 조회
     */
    @Query("SELECT bm FROM BusinessMember bm " +
           "WHERE bm.memberId = :memberId " +
           "AND bm.status = 'ACTIVE'")
    List<BusinessMember> findCurrentBusinessesByMemberId(@Param("memberId") Long memberId);

    /**
     * 월별 신규 가입 회원 통계
     */
    @Query("SELECT EXTRACT(YEAR FROM bm.joinDate) as year, " +
           "EXTRACT(MONTH FROM bm.joinDate) as month, " +
           "COUNT(bm) as count " +
           "FROM BusinessMember bm " +
           "WHERE bm.businessId = :businessId " +
           "AND bm.status = 'ACTIVE' " +
           "AND bm.joinDate >= :startDate " +
           "GROUP BY EXTRACT(YEAR FROM bm.joinDate), EXTRACT(MONTH FROM bm.joinDate) " +
           "ORDER BY year, month")
    List<Object[]> getMonthlyNewMemberStats(@Param("businessId") Long businessId,
                                           @Param("startDate") LocalDate startDate);

    /**
     * SMS 수신동의한 회원 목록 조회
     */
    @Query("SELECT bm FROM BusinessMember bm " +
           "JOIN FETCH bm.member m " +
           "WHERE bm.businessId = :businessId " +
           "AND bm.status = 'ACTIVE' " +
           "AND bm.smsConsent = true")
    List<BusinessMember> findSmsConsentMembersByBusinessId(@Param("businessId") Long businessId);

    /**
     * 특정 기간 동안 방문하지 않은 회원 조회
     */
    @Query("SELECT bm FROM BusinessMember bm " +
           "JOIN FETCH bm.member m " +
           "WHERE bm.businessId = :businessId " +
           "AND bm.status = 'ACTIVE' " +
           "AND (bm.lastVisitDate IS NULL OR bm.lastVisitDate < :date)")
    List<BusinessMember> findInactiveMembersByBusinessId(@Param("businessId") Long businessId, 
                                                        @Param("date") LocalDate date);

    /**
     * 개인레슨 회원 목록 조회 (트레이너가 배정된 회원)
     */
    @Query("SELECT bm FROM BusinessMember bm " +
           "JOIN FETCH bm.member m " +
           "WHERE bm.businessId = :businessId " +
           "AND bm.status = 'ACTIVE' " +
           "AND bm.trainerId IS NOT NULL")
    List<BusinessMember> findPersonalTrainingMembersByBusinessId(@Param("businessId") Long businessId);

    /**
     * 헬스장 일반 회원 목록 조회 (트레이너가 없는 회원)
     */
    @Query("SELECT bm FROM BusinessMember bm " +
           "JOIN FETCH bm.member m " +
           "WHERE bm.businessId = :businessId " +
           "AND bm.status = 'ACTIVE' " +
           "AND bm.trainerId IS NULL")
    List<BusinessMember> findGeneralMembersByBusinessId(@Param("businessId") Long businessId);
}