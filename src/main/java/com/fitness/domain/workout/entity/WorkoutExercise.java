package com.fitness.domain.workout.entity;

import com.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 운동 종목 엔티티
 * 운동일지 내에서 실시한 각 운동 종목의 정보를 관리
 */
@Entity
@Table(name = "workout_exercises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutExercise extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workout_exercise_id")
    private Long workoutExerciseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_session_id", nullable = false)
    private WorkoutSession workoutSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_type_id")
    private ExerciseType exerciseType; // 운동 종목 유형 ID

    @Column(name = "custom_exercise_name", length = 100)
    private String customExerciseName; // 커스텀 운동 종목명

    @Column(name = "exercise_order", nullable = false)
    private Integer exerciseOrder; // 운동 순서

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo; // 종목별 트레이너 메모

    // 연관관계 매핑
    @OneToMany(mappedBy = "workoutExercise", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("setOrder ASC")
    @Builder.Default
    private List<ExerciseSets> exerciseSets = new ArrayList<>();

    @OneToMany(mappedBy = "workoutExercise", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ExerciseAttach> exerciseAttaches = new ArrayList<>();

    // 편의 메서드
    public void addExerciseSet(ExerciseSets exerciseSet) {
        exerciseSets.add(exerciseSet);
        exerciseSet.setWorkoutExercise(this);
    }

    public void removeExerciseSet(ExerciseSets exerciseSet) {
        exerciseSets.remove(exerciseSet);
        exerciseSet.setWorkoutExercise(null);
    }

    public void addExerciseAttach(ExerciseAttach exerciseAttach) {
        exerciseAttaches.add(exerciseAttach);
        exerciseAttach.setWorkoutExercise(this);
    }

    public void removeExerciseAttach(ExerciseAttach exerciseAttach) {
        exerciseAttaches.remove(exerciseAttach);
        exerciseAttach.setWorkoutExercise(null);
    }

    /**
     * 실제 운동명을 반환 (표준 운동명 또는 커스텀 운동명)
     */
    public String getActualExerciseName() {
        if (exerciseType != null) {
            return exerciseType.getName();
        }
        return customExerciseName;
    }

    /**
     * 운동 카테고리를 반환 (표준 카테고리 또는 커스텀 카테고리)
     */
    public String getActualExerciseCategory() {
        if (exerciseType != null) {
            return exerciseType.getExerciseCategory();
        }
        return "기타";
    }
}