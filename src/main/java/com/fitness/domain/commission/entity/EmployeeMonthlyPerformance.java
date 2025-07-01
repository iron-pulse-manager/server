package com.fitness.domain.commission.entity;

import com.fitness.common.BaseEntity;
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
public class EmployeeMonthlyPerformance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_commission_id")
    private Long employeeCommissionId;

    @Column(name = "business_id", nullable = false)
    private Long businessId; // 사업장 ID

    @Column(name = "employee_id", nullable = false)
    private Long employeeId; // 직원 ID

    @Column(name = "year_month", nullable = false, length = 7)
    private String yearMonth; // 년월 (YYYY-MM)

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO; // 총 매출

    @Column(name = "commission_amount", nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal commissionAmount = BigDecimal.ZERO; // 실적 금액

    @Column(name = "new_member_cnt", nullable = false)
    @Builder.Default
    private Integer newMemberCnt = 0; // 신규회원 유치 수

    @Column(name = "renewal_member_cnt", nullable = false)
    @Builder.Default
    private Integer renewalMemberCnt = 0; // 재등록 수

    @Column(name = "managed_member_cnt", nullable = false)
    @Builder.Default
    private Integer managedMemberCnt = 0; // 담당회원 수

    /**
     * YearMonth 객체로 년월 반환
     */
    public YearMonth getYearMonthAsObject() {
        return YearMonth.parse(yearMonth);
    }

    /**
     * YearMonth 객체로 년월 설정
     */
    public void setYearMonthFromObject(YearMonth yearMonth) {
        this.yearMonth = yearMonth.toString();
    }

    /**
     * 총 매출 대비 커미션 비율 계산
     */
    public BigDecimal getCommissionRate() {
        if (totalAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return commissionAmount.divide(totalAmount, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    /**
     * 평균 회원당 매출 계산
     */
    public BigDecimal getAverageSalesPerMember() {
        if (managedMemberCnt == 0) {
            return BigDecimal.ZERO;
        }
        return totalAmount.divide(BigDecimal.valueOf(managedMemberCnt), 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 실적 요약 정보 반환
     */
    public String getPerformanceSummary() {
        return String.format("매출: %s원, 커미션: %s원, 신규: %d명, 재등록: %d명, 담당: %d명",
                totalAmount, commissionAmount, newMemberCnt, renewalMemberCnt, managedMemberCnt);
    }
}