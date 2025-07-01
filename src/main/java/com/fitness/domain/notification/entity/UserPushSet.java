package com.fitness.domain.notification.entity;

import com.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 유저 푸시 설정 엔티티
 * 사용자별 푸시 알림 수신 설정을 관리
 */
@Entity
@Table(name = "user_push_set")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPushSet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_push_id")
    private Long userPushId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "push_id", nullable = false)
    private PushSet pushSet; // 푸시 설정

    @Column(name = "business_id", nullable = false)
    private Long businessId; // 사업장 ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 사용자 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType; // 사용자 유형

    @Column(name = "active_yn", nullable = false)
    @Builder.Default
    private Boolean activeYn = true; // 푸시 활성화 여부

    /**
     * 사용자 유형
     */
    public enum UserType {
        OWNER("사장"),
        EMPLOYEE("직원"),
        MEMBER("회원");

        private final String description;

        UserType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // 연관관계 편의 메서드
    public void setPushSet(PushSet pushSet) {
        this.pushSet = pushSet;
    }

    // 비즈니스 메서드

    /**
     * 푸시 알림 활성화
     */
    public void activatePush() {
        this.activeYn = true;
    }

    /**
     * 푸시 알림 비활성화
     */
    public void deactivatePush() {
        this.activeYn = false;
    }

    /**
     * 푸시 알림이 활성화되어 있는지 확인
     */
    public boolean isPushActive() {
        return Boolean.TRUE.equals(activeYn) && pushSet != null && pushSet.isActive();
    }

    /**
     * 사용자가 사장인지 확인
     */
    public boolean isOwner() {
        return UserType.OWNER.equals(userType);
    }

    /**
     * 사용자가 직원인지 확인
     */
    public boolean isEmployee() {
        return UserType.EMPLOYEE.equals(userType);
    }

    /**
     * 사용자가 회원인지 확인
     */
    public boolean isMember() {
        return UserType.MEMBER.equals(userType);
    }

    /**
     * 특정 푸시 유형에 대해 알림을 받을 수 있는지 확인
     */
    public boolean canReceivePushType(PushSet.PushType pushType) {
        return isPushActive() && pushSet.getPushType().equals(pushType);
    }

    /**
     * 푸시 설정 요약 정보
     */
    public String getUserPushSummary() {
        return String.format("[%s] %s - %s (%s)", 
                userType.getDescription(),
                pushSet != null ? pushSet.getTitle() : "N/A",
                isPushActive() ? "수신" : "차단",
                businessId);
    }

    /**
     * 사용자 정보 업데이트
     */
    public void updateUserInfo(Long businessId, Long userId, UserType userType) {
        this.businessId = businessId;
        this.userId = userId;
        this.userType = userType;
    }
}