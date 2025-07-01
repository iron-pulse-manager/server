package com.fitness.domain.workout.entity;

import com.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * 세트별 상세 기록 엔티티
 * 각 운동 종목의 세트별 무게, 횟수, 시간 등의 상세 기록을 관리
 */
@Entity
@Table(name = "workout_sets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutSet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "set_id")
    private Long setId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_log_id", nullable = false)
    private WorkoutExercise exercise;

    @Column(name = "set_number", nullable = false)
    private Integer setNumber; // 세트 번호

    @Column(name = "weight_kg", precision = 5, scale = 2)
    private BigDecimal weightKg; // 무게 (kg)

    @Column(name = "repetitions")
    private Integer repetitions; // 횟수

    @Column(name = "duration_seconds")
    private Integer durationSeconds; // 시간 (유산소/플랭크 등)

    @Column(name = "distance_meters", precision = 8, scale = 2)
    private BigDecimal distanceMeters; // 거리 (런닝 등)

    @Column(name = "rest_seconds")
    private Integer restSeconds; // 세트 간 휴식시간

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level")
    private DifficultyLevel difficultyLevel; // 체감 난이도

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes; // 세트별 메모

    /**
     * 체감 난이도
     */
    public enum DifficultyLevel {
        EASY("쉬움"),
        NORMAL("보통"),
        HARD("어려움"),
        EXTREME("매우 어려움");

        private final String description;

        DifficultyLevel(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 볼륨 계산 (무게 × 횟수)
     */
    public BigDecimal calculateVolume() {
        if (weightKg != null && repetitions != null) {
            return weightKg.multiply(BigDecimal.valueOf(repetitions));
        }
        return BigDecimal.ZERO;
    }

    /**
     * 세트 요약 정보 (무게 × 횟수 형태로 표시)
     */
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        
        if (weightKg != null && repetitions != null) {
            summary.append(weightKg).append("kg × ").append(repetitions).append("회");
        } else if (durationSeconds != null) {
            summary.append(durationSeconds).append("초");
        } else if (distanceMeters != null) {
            summary.append(distanceMeters).append("m");
        }
        
        return summary.toString();
    }
}