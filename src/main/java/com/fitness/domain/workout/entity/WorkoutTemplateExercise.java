package com.fitness.domain.workout.entity;

import com.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * 템플릿 운동 종목 엔티티
 * 운동 템플릿에 포함된 개별 운동 종목의 추천 설정값을 관리
 */
@Entity
@Table(name = "workout_template_exercises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutTemplateExercise extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_exercise_id")
    private Long templateExerciseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private WorkoutTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private ExerciseType exerciseType; // 표준 운동 종목

    @Column(name = "custom_exercise_name", length = 100)
    private String customExerciseName; // 커스텀 운동명

    @Column(name = "exercise_order", nullable = false)
    private Integer exerciseOrder; // 운동 순서

    @Column(name = "recommended_sets")
    private Integer recommendedSets; // 추천 세트 수

    @Column(name = "recommended_reps_min")
    private Integer recommendedRepsMin; // 추천 최소 횟수

    @Column(name = "recommended_reps_max")
    private Integer recommendedRepsMax; // 추천 최대 횟수

    @Column(name = "recommended_weight_kg", precision = 5, scale = 2)
    private BigDecimal recommendedWeightKg; // 추천 무게

    @Column(name = "rest_seconds")
    private Integer restSeconds; // 세트 간 휴식시간

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes; // 운동별 메모/주의사항

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
     * 운동 카테고리를 반환 (표준 카테고리만 가능)
     */
    public String getExerciseCategory() {
        if (exerciseType != null) {
            return exerciseType.getExerciseCategory();
        }
        return "기타";
    }

    /**
     * 추천 횟수 범위를 문자열로 반환
     */
    public String getRecommendedRepsRange() {
        if (recommendedRepsMin != null && recommendedRepsMax != null) {
            if (recommendedRepsMin.equals(recommendedRepsMax)) {
                return recommendedRepsMin + "회";
            } else {
                return recommendedRepsMin + "-" + recommendedRepsMax + "회";
            }
        } else if (recommendedRepsMin != null) {
            return recommendedRepsMin + "회 이상";
        } else if (recommendedRepsMax != null) {
            return recommendedRepsMax + "회 이하";
        }
        return "자유";
    }

    /**
     * 휴식시간을 분:초 형태로 반환
     */
    public String getFormattedRestTime() {
        if (restSeconds == null) {
            return "자유";
        }
        
        int minutes = restSeconds / 60;
        int seconds = restSeconds % 60;
        
        if (minutes > 0) {
            return minutes + "분 " + seconds + "초";
        } else {
            return seconds + "초";
        }
    }

    /**
     * 템플릿 운동 요약 정보
     */
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(getActualExerciseName());
        
        if (recommendedSets != null) {
            summary.append(" (").append(recommendedSets).append("세트");
            
            if (recommendedRepsMin != null || recommendedRepsMax != null) {
                summary.append(" × ").append(getRecommendedRepsRange());
            }
            
            if (recommendedWeightKg != null) {
                summary.append(" @ ").append(recommendedWeightKg).append("kg");
            }
            
            summary.append(")");
        }
        
        return summary.toString();
    }
}