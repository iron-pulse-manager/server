package com.fitness.domain.workout.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.business.entity.Business;
import com.fitness.domain.business.entity.BusinessEmployee;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 운동일지 템플릿 엔티티
 * 자주 사용하는 운동 루틴을 템플릿으로 저장하여 재사용할 수 있도록 관리
 */
@Entity
@Table(name = "workout_templates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Long templateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private BusinessEmployee trainer;

    @Column(name = "template_name", nullable = false, length = 100)
    private String templateName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "target_muscle_groups", columnDefinition = "JSON")
    private String targetMuscleGroups; // JSON 형태로 타겟 근육군 배열 저장

    @Column(name = "estimated_duration_minutes")
    private Integer estimatedDurationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", nullable = false)
    @Builder.Default
    private DifficultyLevel difficultyLevel = DifficultyLevel.INTERMEDIATE;

    @Column(name = "is_public", nullable = false)
    @Builder.Default
    private Boolean isPublic = false; // 다른 트레이너와 공유 가능

    @Column(name = "usage_count", nullable = false)
    @Builder.Default
    private Integer usageCount = 0; // 사용 횟수

    // 연관관계 매핑
    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("exerciseOrder ASC")
    @Builder.Default
    private List<WorkoutTemplateExercise> exercises = new ArrayList<>();

    // 편의 메서드
    public void addExercise(WorkoutTemplateExercise exercise) {
        exercises.add(exercise);
        exercise.setTemplate(this);
    }

    public void removeExercise(WorkoutTemplateExercise exercise) {
        exercises.remove(exercise);
        exercise.setTemplate(null);
    }

    /**
     * 사용 횟수 증가
     */
    public void incrementUsageCount() {
        this.usageCount = (this.usageCount == null ? 0 : this.usageCount) + 1;
    }

    /**
     * 난이도 레벨
     */
    public enum DifficultyLevel {
        BEGINNER("초급"),
        INTERMEDIATE("중급"),
        ADVANCED("고급");

        private final String description;

        DifficultyLevel(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 템플릿의 총 운동 개수
     */
    public int getTotalExerciseCount() {
        return exercises != null ? exercises.size() : 0;
    }

    /**
     * 템플릿이 공개되어 있고 다른 트레이너가 사용 가능한지 확인
     */
    public boolean isAvailableForOthers() {
        return isPublic != null && isPublic;
    }
}