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
    private MonthlyPerformance performance; // 월별 실적 ID

    @Column(name = "payment_id", nullable = false)
    private Long paymentId; // 결제 ID

    @Column(name = "sales_amount", nullable = false)
    private Integer salesAmount; // 해당 결제의 매출액

    @Column(name = "commission_rate", nullable = false)
    private Integer commissionRate; // 커미션 비율

    @Column(name = "commission_amount", nullable = false)
    private Integer commissionAmount; // 직원의 커미션 금액

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_policy_id")
    private CommissionPolicy commissionPolicy; // 커미션 정책 ID

}