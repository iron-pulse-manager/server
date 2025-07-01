package com.fitness.domain.commission.entity;

import com.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * 직원 실적 계산 내역 엔티티
 * 개별 결제에 대한 커미션 계산 세부내역을 관리
 */
@Entity
@Table(name = "commission_calculations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommissionCalculation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commission_calculation_id")
    private Long commissionCalculationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false)
    private EmployeeMonthlyPerformance performance; // 월별 실적 ID

    @Column(name = "payment_id", nullable = false)
    private Long paymentId; // 결제 ID

    @Column(name = "sales_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal salesAmount; // 해당 결제의 매출액

    @Column(name = "commission_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal commissionRate; // 커미션 비율

    @Column(name = "commission_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal commissionAmount; // 직원의 커미션 금액

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_policy_id")
    private CommissionPolicy commissionPolicy; // 커미션 정책 ID

    // 편의 메서드
    public void setPerformance(EmployeeMonthlyPerformance performance) {
        this.performance = performance;
    }

    public void setCommissionPolicy(CommissionPolicy commissionPolicy) {
        this.commissionPolicy = commissionPolicy;
    }

    /**
     * 커미션이 올바르게 계산되었는지 검증
     */
    public boolean isValidCalculation() {
        BigDecimal expectedCommission = salesAmount.multiply(commissionRate.divide(BigDecimal.valueOf(100)));
        return commissionAmount.compareTo(expectedCommission) == 0;
    }

    /**
     * 커미션 계산 재수행
     */
    public void recalculateCommission() {
        if (commissionPolicy != null) {
            this.commissionRate = commissionPolicy.getCommissionRate();
            this.commissionAmount = commissionPolicy.calculateCommission(salesAmount);
        } else {
            this.commissionAmount = salesAmount.multiply(commissionRate.divide(BigDecimal.valueOf(100)));
        }
    }

    /**
     * 계산 결과 요약
     */
    public String getCalculationSummary() {
        return String.format("매출: %s원 × %s%% = %s원", 
                salesAmount, commissionRate, commissionAmount);
    }

    /**
     * 커미션 정책이 적용되었는지 확인
     */
    public boolean hasPolicyApplied() {
        return commissionPolicy != null;
    }
}