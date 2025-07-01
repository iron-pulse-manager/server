package com.fitness.domain.commission.entity;

import com.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * 커미션 정책 엔티티
 * 사업장별 직원 실적 정책을 관리
 */
@Entity
@Table(name = "commission_policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommissionPolicy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commission_policy_id")
    private Long commissionPolicyId;

    @Column(name = "business_id", nullable = false)
    private Long businessId; // 사업장 ID

    @Column(name = "min_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal minAmount; // 최소 매출액

    @Column(name = "max_amount", precision = 19, scale = 2)
    private BigDecimal maxAmount; // 최대 매출액

    @Column(name = "commission_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal commissionRate; // 커미션 비율 (%)

    @Column(name = "use_yn", nullable = false)
    @Builder.Default
    private Boolean useYn = true; // 사용 여부

    /**
     * 특정 매출액이 이 정책 범위에 해당하는지 확인
     */
    public boolean isApplicable(BigDecimal salesAmount) {
        if (salesAmount.compareTo(minAmount) < 0) {
            return false;
        }
        
        if (maxAmount != null && salesAmount.compareTo(maxAmount) > 0) {
            return false;
        }
        
        return useYn;
    }

    /**
     * 커미션 금액 계산
     */
    public BigDecimal calculateCommission(BigDecimal salesAmount) {
        if (!isApplicable(salesAmount)) {
            return BigDecimal.ZERO;
        }
        
        return salesAmount.multiply(commissionRate.divide(BigDecimal.valueOf(100)));
    }

    /**
     * 정책이 활성화 상태인지 확인
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(useYn);
    }
}