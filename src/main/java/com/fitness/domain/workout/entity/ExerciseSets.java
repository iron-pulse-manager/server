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

    @Column(name = "weight")
    private Integer weight; // 운동 무게 (KG)

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
}