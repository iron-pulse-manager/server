package com.fitness.domain.lesson.entity;

import com.fitness.common.BaseEntity;
import com.fitness.common.enums.LessonType;
import com.fitness.common.enums.ScheduleStatus;
import com.fitness.domain.business.entity.Business;
import com.fitness.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 레슨 일정 엔티티
 * 개별 레슨 및 그룹 레슨 일정을 관리
 */
@Entity
@Table(name = "lesson")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Long lessonId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private User trainer;

    @Enumerated(EnumType.STRING)
    @Column(name = "lesson_type", nullable = false)
    private LessonType lessonType; // 레슨 유형

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private ScheduleStatus status = ScheduleStatus.SCHEDULED; // 스케줄 상태

    @Column(name = "title", nullable = false, length = 200)
    private String title; // 일정 제목

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 일정 메모

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate; // 레슨 시작 일자

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate; // 레슨 시작 일자

    @Column(name = "duration_minute", nullable = false)
    private Integer durationMinute; // 소요시간 (분)

    @Column(name = "max_participant", nullable = false)
    @Builder.Default
    private Integer maxParticipant = 1; // 최대 참가자 수

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LessonParticipant> participants = new ArrayList<>();
}