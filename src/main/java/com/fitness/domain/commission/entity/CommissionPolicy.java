package com.fitness.domain.commission.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.business.entity.Business;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(name = "min_amount", nullable = false)
    private Integer minAmount; // 최소 매출액

    @Column(name = "max_amount")
    private Integer maxAmount; // 최대 매출액

    @Column(name = "commission_rate", nullable = false)
    private Integer commissionRate; // 커미션 비율 (%)

    @Column(name = "use_yn", nullable = false)
    @Builder.Default
    private boolean useYn = true; // 사용 여부

}