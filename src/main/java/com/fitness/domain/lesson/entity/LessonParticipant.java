package com.fitness.domain.lesson.entity;

import com.fitness.common.BaseEntity;
import com.fitness.common.enums.ParticipantStatus;
import com.fitness.domain.business.entity.Business;
import com.fitness.domain.payment.entity.Payment;
import com.fitness.domain.user.entity.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson; // 레슨 일정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private User member; // 신청 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment; // 레슨권 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "participant_status", nullable = false)
    @Builder.Default
    private ParticipantStatus participantStatus = ParticipantStatus.READY; // 참가 상태

    @Column(name = "registered_date")
    private LocalDateTime registeredDate; // 레슨 신청일시

    @Column(name = "approved_date")
    private LocalDateTime approvedDate; // 강사 승인일시
}