package com.fitness.domain.member.repository;

import com.fitness.domain.member.entity.BusinessMember;
import com.fitness.domain.member.entity.BusinessMemberStatus;
import com.fitness.domain.member.entity.QBusinessMember;
import com.fitness.domain.member.entity.QMember;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 사업장-회원 소속관계 커스텀 리포지토리 구현체
 * Querydsl을 사용한 복잡한 조회 메서드 구현
 */
@Repository
@RequiredArgsConstructor
public class BusinessMemberRepositoryImpl implements BusinessMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QBusinessMember businessMember = QBusinessMember.businessMember;
    private final QMember member = QMember.member;

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
            Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        // 필수 조건: 사업장 ID
        builder.and(businessMember.businessId.eq(businessId));

        // 선택적 조건들
        if (name != null && !name.trim().isEmpty()) {
            builder.and(member.name.containsIgnoreCase(name.trim()));
        }

        if (phone != null && !phone.trim().isEmpty()) {
            builder.and(member.phone.containsIgnoreCase(phone.trim()));
        }

        if (memberNumber != null && !memberNumber.trim().isEmpty()) {
            builder.and(businessMember.memberNumber.containsIgnoreCase(memberNumber.trim()));
        }

        if (status != null) {
            builder.and(businessMember.status.eq(status));
        }

        if (trainerId != null) {
            builder.and(businessMember.trainerId.eq(trainerId));
        }

        if (smsConsent != null) {
            builder.and(businessMember.smsConsent.eq(smsConsent));
        }

        if (joinDateFrom != null) {
            builder.and(businessMember.joinDate.goe(joinDateFrom));
        }

        if (joinDateTo != null) {
            builder.and(businessMember.joinDate.loe(joinDateTo));
        }

        if (lastVisitDateFrom != null) {
            builder.and(businessMember.lastVisitDate.goe(lastVisitDateFrom));
        }

        if (lastVisitDateTo != null) {
            builder.and(businessMember.lastVisitDate.loe(lastVisitDateTo));
        }

        // 전체 카운트 조회
        JPAQuery<BusinessMember> countQuery = queryFactory
                .selectFrom(businessMember)
                .join(businessMember.member, member)
                .where(builder);

        long total = countQuery.fetchCount();

        // 데이터 조회 (페이징 적용)
        List<BusinessMember> content = queryFactory
                .selectFrom(businessMember)
                .join(businessMember.member, member).fetchJoin()
                .where(builder)
                .orderBy(businessMember.joinDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public BusinessMemberStats getMemberStatsByBusinessId(Long businessId) {
        return queryFactory
                .select(Projections.bean(BusinessMemberStatsImpl.class,
                        businessMember.count().as("totalMembers"),
                        businessMember.businessMemberId.count()
                                .filter(businessMember.status.eq(BusinessMemberStatus.ACTIVE))
                                .as("activeMembers"),
                        businessMember.businessMemberId.count()
                                .filter(businessMember.status.eq(BusinessMemberStatus.INACTIVE))
                                .as("inactiveMembers"),
                        businessMember.businessMemberId.count()
                                .filter(businessMember.status.eq(BusinessMemberStatus.SUSPENDED))
                                .as("suspendedMembers"),
                        businessMember.businessMemberId.count()
                                .filter(businessMember.status.eq(BusinessMemberStatus.EXPIRED))
                                .as("expiredMembers"),
                        businessMember.businessMemberId.count()
                                .filter(businessMember.trainerId.isNotNull())
                                .as("personalTrainingMembers"),
                        businessMember.businessMemberId.count()
                                .filter(businessMember.trainerId.isNull())
                                .as("generalMembers"),
                        businessMember.businessMemberId.count()
                                .filter(businessMember.smsConsent.isTrue())
                                .as("smsConsentMembers")
                ))
                .from(businessMember)
                .where(businessMember.businessId.eq(businessId))
                .fetchOne();
    }

    @Override
    public List<TrainerMemberStats> getTrainerMemberStatsByBusinessId(Long businessId) {
        return queryFactory
                .select(Projections.bean(TrainerMemberStatsImpl.class,
                        businessMember.trainerId.as("trainerId"),
                        businessMember.count().as("memberCount"),
                        businessMember.businessMemberId.count()
                                .filter(businessMember.joinDate.goe(LocalDate.now().withDayOfMonth(1)))
                                .as("newMembersThisMonth")
                ))
                .from(businessMember)
                .where(
                        businessMember.businessId.eq(businessId),
                        businessMember.trainerId.isNotNull(),
                        businessMember.status.eq(BusinessMemberStatus.ACTIVE)
                )
                .groupBy(businessMember.trainerId)
                .fetch();
    }

    @Override
    public List<MonthlyMemberTrend> getMonthlyMemberTrend(Long businessId, LocalDate startDate, LocalDate endDate) {
        // 복잡한 월별 추이 쿼리 구현
        // 가입/탈퇴 추이를 계산하는 로직 필요
        return List.of(); // 실제 구현 시 상세 로직 추가
    }

    @Override
    public List<BusinessMember> findLowActivityMembers(Long businessId, int daysThreshold) {
        LocalDate thresholdDate = LocalDate.now().minusDays(daysThreshold);
        
        return queryFactory
                .selectFrom(businessMember)
                .join(businessMember.member, member).fetchJoin()
                .where(
                        businessMember.businessId.eq(businessId),
                        businessMember.status.eq(BusinessMemberStatus.ACTIVE),
                        businessMember.lastVisitDate.lt(thresholdDate)
                                .or(businessMember.lastVisitDate.isNull())
                )
                .orderBy(businessMember.lastVisitDate.asc().nullsFirst())
                .fetch();
    }

    @Override
    public List<BusinessMember> findExpiringSoonMembers(Long businessId, int daysUntilExpiry) {
        // 이용권 만료일 기준으로 만료 임박 회원 조회
        // payments 테이블과 조인하여 구현 필요
        return queryFactory
                .selectFrom(businessMember)
                .join(businessMember.member, member).fetchJoin()
                .where(
                        businessMember.businessId.eq(businessId),
                        businessMember.status.eq(BusinessMemberStatus.ACTIVE)
                        // TODO: payments 테이블과 조인하여 만료일 기준 조건 추가
                )
                .fetch();
    }

    @Override
    public List<BusinessMember> findRecentNewMembers(Long businessId, int recentDays) {
        LocalDate recentDate = LocalDate.now().minusDays(recentDays);
        
        return queryFactory
                .selectFrom(businessMember)
                .join(businessMember.member, member).fetchJoin()
                .where(
                        businessMember.businessId.eq(businessId),
                        businessMember.joinDate.goe(recentDate),
                        businessMember.status.eq(BusinessMemberStatus.ACTIVE)
                )
                .orderBy(businessMember.joinDate.desc())
                .fetch();
    }

    @Override
    public List<BusinessMember> findReturningMembers(Long businessId, LocalDate periodStart, LocalDate periodEnd) {
        // 재등록 회원 분석 로직
        // 이전에 만료되었다가 다시 가입한 회원들을 찾는 복잡한 쿼리
        return queryFactory
                .selectFrom(businessMember)
                .join(businessMember.member, member).fetchJoin()
                .where(
                        businessMember.businessId.eq(businessId),
                        businessMember.joinDate.between(periodStart, periodEnd),
                        businessMember.status.eq(BusinessMemberStatus.ACTIVE)
                        // TODO: 재등록 여부를 판단하는 추가 조건 구현
                )
                .fetch();
    }

    // 통계용 DTO 구현 클래스들
    public static class BusinessMemberStatsImpl implements BusinessMemberStats {
        private Long totalMembers;
        private Long activeMembers;
        private Long inactiveMembers;
        private Long suspendedMembers;
        private Long expiredMembers;
        private Long personalTrainingMembers;
        private Long generalMembers;
        private Long smsConsentMembers;
        private Double averageVisitFrequency;

        // Getter/Setter methods
        @Override public Long getTotalMembers() { return totalMembers; }
        public void setTotalMembers(Long totalMembers) { this.totalMembers = totalMembers; }
        
        @Override public Long getActiveMembers() { return activeMembers; }
        public void setActiveMembers(Long activeMembers) { this.activeMembers = activeMembers; }
        
        @Override public Long getInactiveMembers() { return inactiveMembers; }
        public void setInactiveMembers(Long inactiveMembers) { this.inactiveMembers = inactiveMembers; }
        
        @Override public Long getSuspendedMembers() { return suspendedMembers; }
        public void setSuspendedMembers(Long suspendedMembers) { this.suspendedMembers = suspendedMembers; }
        
        @Override public Long getExpiredMembers() { return expiredMembers; }
        public void setExpiredMembers(Long expiredMembers) { this.expiredMembers = expiredMembers; }
        
        @Override public Long getPersonalTrainingMembers() { return personalTrainingMembers; }
        public void setPersonalTrainingMembers(Long personalTrainingMembers) { this.personalTrainingMembers = personalTrainingMembers; }
        
        @Override public Long getGeneralMembers() { return generalMembers; }
        public void setGeneralMembers(Long generalMembers) { this.generalMembers = generalMembers; }
        
        @Override public Long getSmsConsentMembers() { return smsConsentMembers; }
        public void setSmsConsentMembers(Long smsConsentMembers) { this.smsConsentMembers = smsConsentMembers; }
        
        @Override public Double getAverageVisitFrequency() { return averageVisitFrequency; }
        public void setAverageVisitFrequency(Double averageVisitFrequency) { this.averageVisitFrequency = averageVisitFrequency; }
    }

    public static class TrainerMemberStatsImpl implements TrainerMemberStats {
        private Long trainerId;
        private String trainerName;
        private Long memberCount;
        private Double averageSessionsPerMember;
        private Long newMembersThisMonth;

        @Override public Long getTrainerId() { return trainerId; }
        public void setTrainerId(Long trainerId) { this.trainerId = trainerId; }
        
        @Override public String getTrainerName() { return trainerName; }
        public void setTrainerName(String trainerName) { this.trainerName = trainerName; }
        
        @Override public Long getMemberCount() { return memberCount; }
        public void setMemberCount(Long memberCount) { this.memberCount = memberCount; }
        
        @Override public Double getAverageSessionsPerMember() { return averageSessionsPerMember; }
        public void setAverageSessionsPerMember(Double averageSessionsPerMember) { this.averageSessionsPerMember = averageSessionsPerMember; }
        
        @Override public Long getNewMembersThisMonth() { return newMembersThisMonth; }
        public void setNewMembersThisMonth(Long newMembersThisMonth) { this.newMembersThisMonth = newMembersThisMonth; }
    }
}