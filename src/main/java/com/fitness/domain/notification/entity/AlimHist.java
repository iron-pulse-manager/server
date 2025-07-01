package com.fitness.domain.notification.entity;

import com.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 알림 내역 엔티티
 * 시스템 내 모든 알림 발송 및 수신 내역을 관리
 */
@Entity
@Table(name = "alim_hist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlimHist extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alim_hist_id")
    private Long alimHistId;

    @Column(name = "business_id", nullable = false)
    private Long businessId; // 사업장 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    private DeviceType deviceType; // 디바이스 유형

    @Enumerated(EnumType.STRING)
    @Column(name = "alim_type", nullable = false)
    private AlimType alimType; // 알림 유형

    @Column(name = "sender_id", nullable = false)
    private Long senderId; // 발신자 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type", nullable = false)
    private UserType senderType; // 발신자 유형

    @Column(name = "recipient_id", nullable = false)
    private Long recipientId; // 수신자 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "recipient_type", nullable = false)
    private UserType recipientType; // 수신자 유형

    @Column(name = "title", length = 200)
    private String title; // 알림 제목

    @Column(name = "message", columnDefinition = "TEXT")
    private String message; // 알림 내용

    @Column(name = "send_date", nullable = false)
    private LocalDateTime sendDate; // 발송일시

    @Column(name = "read_date")
    private LocalDateTime readDate; // 수신일시

    @Column(name = "reference_id")
    private Long referenceId; // 참조 ID (연관된 게시글, 댓글 등의 ID)

    @Column(name = "reference_type", length = 50)
    private String referenceType; // 참조 타입 (post, comment, lesson, schedule 등)

    /**
     * 디바이스 유형
     */
    public enum DeviceType {
        WEB("웹"),
        APP("앱");

        private final String description;

        DeviceType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 알림 유형
     */
    public enum AlimType {
        LESSON_REQUEST("레슨 신청"),
        LESSON_APPROVED("레슨 승인"),
        LESSON_REJECTED("레슨 거절"),
        LESSON_CANCELLED("레슨 취소"),
        WORKOUT_COMMENT("운동 댓글"),
        WORKOUT_REPLY("운동 댓글 답글"),
        COMMUNITY_COMMENT("커뮤니티 댓글"),
        COMMUNITY_REPLY("커뮤니티 답글"),
        SCHEDULE_PARTICIPANT("일정 참여자 등록"),
        MEMBERSHIP_EXPIRING("회원권 만료 임박"),
        PAYMENT_COMPLETED("결제 완료"),
        OUTSTANDING_PAYMENT("미수금 결제"),
        SYSTEM_NOTICE("시스템 공지");

        private final String description;

        AlimType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

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

    // 비즈니스 메서드

    /**
     * 알림 읽음 처리
     */
    public void markAsRead() {
        if (this.readDate == null) {
            this.readDate = LocalDateTime.now();
        }
    }

    /**
     * 읽지 않은 알림인지 확인
     */
    public boolean isUnread() {
        return readDate == null;
    }

    /**
     * 읽은 알림인지 확인
     */
    public boolean isRead() {
        return readDate != null;
    }

    /**
     * 웹 알림인지 확인
     */
    public boolean isWebNotification() {
        return DeviceType.WEB.equals(deviceType);
    }

    /**
     * 앱 알림인지 확인
     */
    public boolean isAppNotification() {
        return DeviceType.APP.equals(deviceType);
    }

    /**
     * 발신자가 직원인지 확인
     */
    public boolean isSentByEmployee() {
        return UserType.EMPLOYEE.equals(senderType);
    }

    /**
     * 발신자가 회원인지 확인
     */
    public boolean isSentByMember() {
        return UserType.MEMBER.equals(senderType);
    }

    /**
     * 수신자가 직원인지 확인
     */
    public boolean isReceivedByEmployee() {
        return UserType.EMPLOYEE.equals(recipientType);
    }

    /**
     * 수신자가 회원인지 확인
     */
    public boolean isReceivedByMember() {
        return UserType.MEMBER.equals(recipientType);
    }

    /**
     * 참조 정보가 있는지 확인
     */
    public boolean hasReference() {
        return referenceId != null && referenceType != null;
    }

    /**
     * 알림 요약 정보 반환
     */
    public String getNotificationSummary() {
        return String.format("[%s] %s → %s: %s", 
                alimType.getDescription(), 
                senderType.getDescription(), 
                recipientType.getDescription(),
                title != null ? title : message);
    }
}