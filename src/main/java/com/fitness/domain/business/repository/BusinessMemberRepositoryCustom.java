package com.fitness.domain.business.repository;

import com.fitness.domain.business.entity.BusinessMember;
import com.fitness.domain.business.entity.BusinessMemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * 사업장-회원 소속관계 커스텀 리포지토리 인터페이스
 * Querydsl을 사용한 복잡한 조회 메서드 정의
 */
public interface BusinessMemberRepositoryCustom {

    /**
     * 복잡한 조건으로 회원 검색
     */
    Page<BusinessMember> searchMembersWithConditions(
            Long businessId,
            String name,
            String phone,
            String memberNumber,
            BusinessMemberStatus status,
            Long trainerId,
            Boolean smsConsent,
            LocalDate joinDateFrom,
            LocalDate joinDateTo,
            LocalDate lastVisitDateFrom,
            LocalDate lastVisitDateTo,
            Pageable pageable
    );

    /**
     * 특정 사업장의 회원 통계 조회
     */
    BusinessMemberStats getMemberStatsByBusinessId(Long businessId);

    /**
     * 트레이너별 담당 회원 통계
     */
    List<TrainerMemberStats> getTrainerMemberStatsByBusinessId(Long businessId);

    /**
     * 월별 회원 가입/탈퇴 추이
     */
    List<MonthlyMemberTrend> getMonthlyMemberTrend(Long businessId, LocalDate startDate, LocalDate endDate);

    /**
     * 회원 활동성 분석 (방문 빈도 기준)
     */
    List<BusinessMember> findLowActivityMembers(Long businessId, int daysThreshold);

    /**
     * 만료 임박 회원 목록 조회 (이용권 만료일 기준)
     */
    List<BusinessMember> findExpiringSoonMembers(Long businessId, int daysUntilExpiry);

    /**
     * 신규 가입 회원 목록 조회 (최근 N일)
     */
    List<BusinessMember> findRecentNewMembers(Long businessId, int recentDays);

    /**
     * 재등록 회원 분석
     */
    List<BusinessMember> findReturningMembers(Long businessId, LocalDate periodStart, LocalDate periodEnd);

    // 통계용 DTO 클래스들
    interface BusinessMemberStats {
        Long getTotalMembers();
        Long getActiveMembers();
        Long getInactiveMembers();
        Long getSuspendedMembers();
        Long getExpiredMembers();
        Long getPersonalTrainingMembers();
        Long getGeneralMembers();
        Long getSmsConsentMembers();
        Double getAverageVisitFrequency();
    }

    interface TrainerMemberStats {
        Long getTrainerId();
        String getTrainerName();
        Long getMemberCount();
        Double getAverageSessionsPerMember();
        Long getNewMembersThisMonth();
    }

    interface MonthlyMemberTrend {
        Integer getYear();
        Integer getMonth();
        Long getNewMembers();
        Long getExpiredMembers();
        Long getNetGrowth();
        Long getTotalActiveMembers();
    }
}