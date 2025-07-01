package com.fitness.domain.locker.entity;

import com.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * 락커 배정 엔티티
 * 회원에게 배정된 락커 정보를 관리
 */
@Entity
@Table(name = "locker_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LockerAssign extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "locker_assign_id")
    private Long lockerAssignId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locker_id", nullable = false)
    private Locker locker; // 락커

    @Column(name = "member_id", nullable = false)
    private Long memberId; // 회원 ID

    @Column(name = "payment_id")
    private Long paymentId; // 락커 결제 ID

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate; // 락커 사용 시작일

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate; // 락커 사용 종료일

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private AssignmentStatus status = AssignmentStatus.ACTIVE; // 배정 상태

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo; // 메모

    /**
     * 배정 상태
     */
    public enum AssignmentStatus {
        ACTIVE("사용중"),
        EXPIRED("만료"),
        SUSPENDED("일시중지"),
        CANCELLED("취소");

        private final String description;

        AssignmentStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // 연관관계 편의 메서드
    public void setLocker(Locker locker) {
        this.locker = locker;
    }

    // 비즈니스 메서드

    /**
     * 락커 사용 만료 처리
     */
    public void expire() {
        this.status = AssignmentStatus.EXPIRED;
        if (locker != null) {
            locker.expire();
        }
    }

    /**
     * 락커 사용 일시 중지
     */
    public void suspend() {
        if (AssignmentStatus.ACTIVE.equals(status)) {
            this.status = AssignmentStatus.SUSPENDED;
        }
    }

    /**
     * 락커 사용 재개
     */
    public void resume() {
        if (AssignmentStatus.SUSPENDED.equals(status)) {
            this.status = AssignmentStatus.ACTIVE;
        }
    }

    /**
     * 락커 배정 취소
     */
    public void cancel() {
        this.status = AssignmentStatus.CANCELLED;
        if (locker != null) {
            locker.endUse();
        }
    }

    /**
     * 활성 상태인지 확인
     */
    public boolean isActive() {
        return AssignmentStatus.ACTIVE.equals(status);
    }

    /**
     * 만료된 상태인지 확인
     */
    public boolean isExpired() {
        return AssignmentStatus.EXPIRED.equals(status);
    }

    /**
     * 일시 중지된 상태인지 확인
     */
    public boolean isSuspended() {
        return AssignmentStatus.SUSPENDED.equals(status);
    }

    /**
     * 취소된 상태인지 확인
     */
    public boolean isCancelled() {
        return AssignmentStatus.CANCELLED.equals(status);
    }

    /**
     * 만료 예정인지 확인 (7일 이내)
     */
    public boolean isExpiringWithin(int days) {
        LocalDate checkDate = LocalDate.now().plusDays(days);
        return endDate.isBefore(checkDate) || endDate.isEqual(checkDate);
    }

    /**
     * 사용 가능한 락커인지 확인
     */
    public boolean canUse() {
        return AssignmentStatus.ACTIVE.equals(status) && 
               !LocalDate.now().isAfter(endDate);
    }

    /**
     * 사용 기간 연장
     */
    public void extendPeriod(LocalDate newEndDate) {
        if (newEndDate.isAfter(this.endDate)) {
            this.endDate = newEndDate;
            if (AssignmentStatus.EXPIRED.equals(status)) {
                this.status = AssignmentStatus.ACTIVE;
            }
        }
    }

    /**
     * 배정 정보 업데이트
     */
    public void updateAssignmentInfo(LocalDate startDate, LocalDate endDate, String memo) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.memo = memo;
    }

    /**
     * 남은 사용 일수 계산
     */
    public long getRemainingDays() {
        LocalDate today = LocalDate.now();
        if (today.isAfter(endDate)) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(today, endDate);
    }

    /**
     * 총 사용 일수 계산
     */
    public long getTotalDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    /**
     * 배정 상태 요약
     */
    public String getAssignmentSummary() {
        return String.format("회원ID: %d, 기간: %s ~ %s, 상태: %s", 
                memberId, 
                startDate, 
                endDate, 
                status.getDescription());
    }
}