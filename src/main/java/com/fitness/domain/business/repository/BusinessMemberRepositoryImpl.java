package com.fitness.domain.business.repository;

import com.fitness.domain.business.entity.BusinessMember;
import com.fitness.domain.business.entity.BusinessMember.BusinessMemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * BusinessMember 커스텀 리포지토리 구현체
 * 임시로 기본 구현만 제공 - 추후 QueryDSL로 구현 예정
 */
@Repository
public class BusinessMemberRepositoryImpl implements BusinessMemberRepositoryCustom {

    @Override
    public Page<BusinessMember> searchMembersWithConditions(
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
    ) {
        // TODO: QueryDSL 구현 필요 - 임시로 빈 결과 반환
        return new PageImpl<>(List.of(), pageable, 0);
    }

    @Override
    public BusinessMemberStats getMemberStatsByBusinessId(Long businessId) {
        // TODO: 구현 필요
        return null;
    }

    @Override
    public List<TrainerMemberStats> getTrainerMemberStatsByBusinessId(Long businessId) {
        // TODO: 구현 필요
        return List.of();
    }

    @Override
    public List<MonthlyMemberTrend> getMonthlyMemberTrend(Long businessId, LocalDate startDate, LocalDate endDate) {
        // TODO: 구현 필요
        return List.of();
    }

    @Override
    public List<BusinessMember> findLowActivityMembers(Long businessId, int daysThreshold) {
        // TODO: 구현 필요
        return List.of();
    }

    @Override
    public List<BusinessMember> findExpiringSoonMembers(Long businessId, int daysUntilExpiry) {
        // TODO: 구현 필요
        return List.of();
    }

    @Override
    public List<BusinessMember> findRecentNewMembers(Long businessId, int recentDays) {
        // TODO: 구현 필요
        return List.of();
    }

    @Override
    public List<BusinessMember> findReturningMembers(Long businessId, LocalDate periodStart, LocalDate periodEnd) {
        // TODO: 구현 필요
        return List.of();
    }
}