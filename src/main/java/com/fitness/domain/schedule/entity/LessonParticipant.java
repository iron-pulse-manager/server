package com.fitness.domain.schedule.entity;

import com.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 레슨 일정 참여 회원 엔티티
 * 레슨에 참여하는 회원들의 정보를 관리
 */
@Entity
@Table(name = "lesson_participants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonParticipant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_participant_id")
    private Long lessonParticipantId;

    @Column(name = "business_id", nullable = false)
    private Long businessId; // 사업장 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_schedule_id", nullable = false)
    private LessonSchedule lessonSchedule; // 레슨 일정

    @Column(name = "member_id", nullable = false)
    private Long memberId; // 회원 ID

    @Column(name = "payment_id")
    private Long paymentId; // 레슨권 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "participant_status", nullable = false)
    @Builder.Default
    private ParticipantStatus participantStatus = ParticipantStatus.PENDING; // 참가 상태

    @Column(name = "registered_at")
    private LocalDateTime registeredAt; // 레슨 신청시간

    @Column(name = "approved_at")
    private LocalDateTime approvedAt; // 강사 승인시간

    @Column(name = "attended_at")
    private LocalDateTime attendedAt; // 출석 시간

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo; // 메모

    /**
     * 참가 상태
     */
    public enum ParticipantStatus {
        PENDING("승인대기"),
        APPROVED("승인완료"),
        ATTENDED("출석"),
        ABSENT("미출석"),
        CANCELLED("취소");

        private final String description;

        ParticipantStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // 연관관계 편의 메서드
    public void setLessonSchedule(LessonSchedule lessonSchedule) {
        this.lessonSchedule = lessonSchedule;
    }

    // 비즈니스 메서드

    /**
     * 레슨 참여 승인
     */
    public void approve() {
        if (ParticipantStatus.PENDING.equals(participantStatus)) {
            this.participantStatus = ParticipantStatus.APPROVED;
            this.approvedAt = LocalDateTime.now();
        }
    }

    /**
     * 레슨 참여 취소
     */
    public void cancel() {
        if (!ParticipantStatus.ATTENDED.equals(participantStatus)) {
            this.participantStatus = ParticipantStatus.CANCELLED;
        }
    }

    /**
     * 출석 처리
     */
    public void markAttended() {
        if (ParticipantStatus.APPROVED.equals(participantStatus)) {
            this.participantStatus = ParticipantStatus.ATTENDED;
            this.attendedAt = LocalDateTime.now();
        }
    }

    /**
     * 미출석 처리
     */
    public void markAbsent() {
        if (ParticipantStatus.APPROVED.equals(participantStatus)) {
            this.participantStatus = ParticipantStatus.ABSENT;
        }
    }

    /**
     * 승인 대기 상태인지 확인
     */
    public boolean isPending() {
        return ParticipantStatus.PENDING.equals(participantStatus);
    }

    /**
     * 승인된 상태인지 확인
     */
    public boolean isApproved() {
        return ParticipantStatus.APPROVED.equals(participantStatus);
    }

    /**
     * 출석한 상태인지 확인
     */
    public boolean isAttended() {
        return ParticipantStatus.ATTENDED.equals(participantStatus);
    }

    /**
     * 미출석 상태인지 확인
     */
    public boolean isAbsent() {
        return ParticipantStatus.ABSENT.equals(participantStatus);
    }

    /**
     * 취소된 상태인지 확인
     */
    public boolean isCancelled() {
        return ParticipantStatus.CANCELLED.equals(participantStatus);
    }

    /**
     * 레슨에 실제 참여 가능한 상태인지 확인
     */
    public boolean canParticipate() {
        return ParticipantStatus.APPROVED.equals(participantStatus);
    }

    /**
     * 신청 시간 설정
     */
    public void setRegistrationTime() {
        if (this.registeredAt == null) {
            this.registeredAt = LocalDateTime.now();
        }
    }

    /**
     * 참여자 정보 업데이트
     */
    public void updateParticipantInfo(Long paymentId, String memo) {
        this.paymentId = paymentId;
        this.memo = memo;
    }

    /**
     * 참여자 상태 요약
     */
    public String getParticipantSummary() {
        return String.format("회원ID: %d, 상태: %s, 신청: %s", 
                memberId, 
                participantStatus.getDescription(),
                registeredAt != null ? registeredAt.toString() : "N/A");
    }
}