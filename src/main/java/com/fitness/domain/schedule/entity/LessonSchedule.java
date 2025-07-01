package com.fitness.domain.schedule.entity;

import com.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 레슨 일정 엔티티
 * 개별 레슨 및 그룹 레슨 일정을 관리
 */
@Entity
@Table(name = "lesson_schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_schedule_id")
    private Long lessonScheduleId;

    @Column(name = "business_id", nullable = false)
    private Long businessId; // 사업장 ID

    @Column(name = "employee_id", nullable = false)
    private Long employeeId; // 강사 ID

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

    @Column(name = "schedule_date", nullable = false)
    private LocalDate scheduleDate; // 레슨 일자

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime; // 레슨 시작시간

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime; // 레슨 종료시간

    @Column(name = "duration_minute", nullable = false)
    private Integer durationMinute; // 소요시간 (분)

    @Column(name = "max_participant", nullable = false)
    @Builder.Default
    private Integer maxParticipant = 1; // 최대 참가자 수

    @Column(name = "current_participant", nullable = false)
    @Builder.Default
    private Integer currentParticipant = 0; // 현재 참가자 수

    // 연관관계 매핑
    @OneToMany(mappedBy = "lessonSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LessonParticipant> participants = new ArrayList<>();

    /**
     * 레슨 유형
     */
    public enum LessonType {
        PERSONAL_PT("1:1 PT"),
        GROUP_PT("그룹 PT"),
        ONLINE_PT("온라인 PT"),
        PERSONAL_PILATES("필라테스 개인레슨"),
        GROUP_PILATES("필라테스 그룹레슨"),
        YOGA("요가"),
        SPINNING("스피닝"),
        CROSSFIT("크로스핏");

        private final String description;

        LessonType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 스케줄 상태
     */
    public enum ScheduleStatus {
        SCHEDULED("진행예정"),
        IN_PROGRESS("진행중"),
        COMPLETED("완료"),
        CANCELLED("취소");

        private final String description;

        ScheduleStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // 편의 메서드
    public void addParticipant(LessonParticipant participant) {
        participants.add(participant);
        participant.setLessonSchedule(this);
        this.currentParticipant = participants.size();
    }

    public void removeParticipant(LessonParticipant participant) {
        participants.remove(participant);
        participant.setLessonSchedule(null);
        this.currentParticipant = participants.size();
    }

    // 비즈니스 메서드

    /**
     * 레슨이 개인 레슨인지 확인
     */
    public boolean isPersonalLesson() {
        return LessonType.PERSONAL_PT.equals(lessonType) || 
               LessonType.PERSONAL_PILATES.equals(lessonType);
    }

    /**
     * 레슨이 그룹 레슨인지 확인
     */
    public boolean isGroupLesson() {
        return LessonType.GROUP_PT.equals(lessonType) || 
               LessonType.GROUP_PILATES.equals(lessonType) ||
               LessonType.YOGA.equals(lessonType) ||
               LessonType.SPINNING.equals(lessonType) ||
               LessonType.CROSSFIT.equals(lessonType);
    }

    /**
     * 참가 가능한지 확인
     */
    public boolean canJoin() {
        return ScheduleStatus.SCHEDULED.equals(status) && 
               currentParticipant < maxParticipant;
    }

    /**
     * 레슨 시작
     */
    public void startLesson() {
        if (ScheduleStatus.SCHEDULED.equals(status)) {
            this.status = ScheduleStatus.IN_PROGRESS;
        }
    }

    /**
     * 레슨 완료
     */
    public void completeLesson() {
        if (ScheduleStatus.IN_PROGRESS.equals(status)) {
            this.status = ScheduleStatus.COMPLETED;
        }
    }

    /**
     * 레슨 취소
     */
    public void cancelLesson() {
        if (ScheduleStatus.SCHEDULED.equals(status) || 
            ScheduleStatus.IN_PROGRESS.equals(status)) {
            this.status = ScheduleStatus.CANCELLED;
        }
    }

    /**
     * 레슨 정보 업데이트
     */
    public void updateLessonInfo(String title, String description, 
                                LocalDate scheduleDate, LocalTime startTime, LocalTime endTime) {
        this.title = title;
        this.description = description;
        this.scheduleDate = scheduleDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinute = calculateDuration(startTime, endTime);
    }

    /**
     * 소요 시간 계산
     */
    private Integer calculateDuration(LocalTime startTime, LocalTime endTime) {
        return (int) java.time.Duration.between(startTime, endTime).toMinutes();
    }

    /**
     * 레슨 요약 정보
     */
    public String getLessonSummary() {
        return String.format("[%s] %s - %s (%d/%d명)", 
                lessonType.getDescription(), 
                title, 
                status.getDescription(),
                currentParticipant, 
                maxParticipant);
    }

    /**
     * 만석인지 확인
     */
    public boolean isFull() {
        return currentParticipant >= maxParticipant;
    }

    /**
     * 레슨이 활성 상태인지 확인
     */
    public boolean isActive() {
        return ScheduleStatus.SCHEDULED.equals(status) || 
               ScheduleStatus.IN_PROGRESS.equals(status);
    }
}