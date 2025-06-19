package com.gymmanager.domain.user.entity;

import com.gymmanager.common.entity.BaseEntity;
import com.gymmanager.domain.business.entity.Business;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 사용자-사업장 매핑 엔터티
 * 
 * 사용자가 여러 사업장을 관리할 수 있는 관계를 나타냅니다.
 * 사업장 연결 요청의 승인/거절 상태도 관리합니다.
 */
@Entity
@Table(
    name = "user_business_mappings",
    uniqueConstraints = @UniqueConstraint(
        name = "unique_user_business", 
        columnNames = {"user_id", "business_id"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBusinessMapping extends BaseEntity {

    /**
     * 사용자 참조
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_business_user"))
    private User user;

    /**
     * 사업장 참조
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_business_business"))
    private Business business;

    /**
     * 승인 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false)
    @Builder.Default
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    /**
     * 요청 일시 (생성 시 자동 설정)
     */
    @Column(name = "requested_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime requestedAt = LocalDateTime.now();

    /**
     * 승인/거절 처리 일시
     */
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    /**
     * 승인 상태 열거형
     */
    public enum ApprovalStatus {
        PENDING("승인대기"),
        APPROVED("승인완료"),
        REJECTED("거절");

        private final String description;

        ApprovalStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 비즈니스 로직: 승인 처리
     */
    public void approve() {
        this.approvalStatus = ApprovalStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();
    }

    /**
     * 비즈니스 로직: 거절 처리
     */
    public void reject() {
        this.approvalStatus = ApprovalStatus.REJECTED;
        this.approvedAt = LocalDateTime.now();
    }

    /**
     * 비즈니스 로직: 승인된 매핑인지 확인
     */
    public boolean isApproved() {
        return ApprovalStatus.APPROVED.equals(approvalStatus);
    }

    /**
     * 비즈니스 로직: 대기 중인 매핑인지 확인
     */
    public boolean isPending() {
        return ApprovalStatus.PENDING.equals(approvalStatus);
    }

    /**
     * 비즈니스 로직: 거절된 매핑인지 확인
     */
    public boolean isRejected() {
        return ApprovalStatus.REJECTED.equals(approvalStatus);
    }

    /**
     * 비즈니스 로직: 사용자가 해당 사업장에 접근 가능한지 확인
     */
    public boolean canAccess() {
        return isApproved() && business.isActive();
    }

    @Override
    public String toString() {
        return "UserBusinessMapping{" +
                "id=" + getId() +
                ", userId=" + (user != null ? user.getId() : null) +
                ", businessId=" + (business != null ? business.getId() : null) +
                ", approvalStatus=" + approvalStatus +
                ", requestedAt=" + requestedAt +
                ", approvedAt=" + approvedAt +
                '}';
    }
}