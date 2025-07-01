package com.fitness.domain.workout.entity;

import com.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * 운동 종목 세트 기록 엔티티
 * 각 운동 종목의 세트별 상세 기록을 관리
 */
@Entity
@Table(name = "exercise_sets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseSets extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_sets_id")
    private Long exerciseSetsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_exercise_id", nullable = false)
    private WorkoutExercise workoutExercise;

    @Column(name = "set_order", nullable = false)
    private Integer setOrder; // 세트 순서

    @Column(name = "weight", precision = 5, scale = 2)
    private BigDecimal weight; // 운동 무게 (KG)

    @Column(name = "reps")
    private Integer reps; // 수행 횟수

    @Column(name = "duration_second")
    private Integer durationSecond; // 수행 시간 (초)

    @Column(name = "duration_minute")
    private Integer durationMinute; // 수행 시간 (분)

    @Column(name = "rest_minute")
    private Integer restMinute; // 세트 간 쉬는시간 (분)

    @Column(name = "rest_second")
    private Integer restSecond; // 세트 간 쉬는시간 (초)

    /**
     * 총 수행 시간을 초 단위로 반환
     */
    public int getTotalDurationInSeconds() {
        int totalSeconds = 0;
        if (durationMinute != null) {
            totalSeconds += durationMinute * 60;
        }
        if (durationSecond != null) {
            totalSeconds += durationSecond;
        }
        return totalSeconds;
    }

    /**
     * 총 휴식 시간을 초 단위로 반환
     */
    public int getTotalRestInSeconds() {
        int totalSeconds = 0;
        if (restMinute != null) {
            totalSeconds += restMinute * 60;
        }
        if (restSecond != null) {
            totalSeconds += restSecond;
        }
        return totalSeconds;
    }

    /**
     * 세트 정보 요약 문자열 반환
     */
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(setOrder).append("세트: ");
        
        if (weight != null) {
            summary.append(weight).append("kg ");
        }
        
        if (reps != null) {
            summary.append(reps).append("회 ");
        }
        
        int duration = getTotalDurationInSeconds();
        if (duration > 0) {
            if (duration >= 60) {
                summary.append(duration / 60).append("분 ");
                if (duration % 60 > 0) {
                    summary.append(duration % 60).append("초 ");
                }
            } else {
                summary.append(duration).append("초 ");
            }
        }
        
        return summary.toString().trim();
    }
}