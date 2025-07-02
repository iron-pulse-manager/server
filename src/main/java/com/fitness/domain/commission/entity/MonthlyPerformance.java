package com.fitness.domain.commission.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.business.entity.Business;
import com.fitness.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.YearMonth;

/**
 * 직원 월별 실적 엔티티
 * 직원의 월별 매출 및 회원 관리 실적을 관리
 */
@Entity
@Table(name = "employee_monthly_performances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyPerformance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "monthly_performance_id")
    private Long monthlyPerformanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private User employeeId; // 직원 ID

    @Column(name = "year_month", nullable = false, length = 7)
    private String yearMonth; // 년월 (YYYY-MM)

    @Column(name = "total_amount", nullable = false)
    @Builder.Default
    private Integer totalAmount = 0; // 총 매출

    @Column(name = "commission_amount", nullable = false)
    @Builder.Default
    private Integer commissionAmount = 0; // 실적 금액

    @Column(name = "new_member_cnt", nullable = false)
    @Builder.Default
    private Integer newMemberCnt = 0; // 신규회원 유치 수

    @Column(name = "renewal_member_cnt", nullable = false)
    @Builder.Default
    private Integer renewalMemberCnt = 0; // 재등록 수

    @Column(name = "managed_member_cnt", nullable = false)
    @Builder.Default
    private Integer managedMemberCnt = 0; // 담당회원 수

}