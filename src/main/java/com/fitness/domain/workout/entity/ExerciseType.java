package com.fitness.domain.workout.entity;

import com.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 운동 종목 유형 엔티티
 * 운동 종목의 마스터 데이터를 관리
 */
@Entity
@Table(name = "exercise_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_type_id")
    private Long exerciseTypeId;

    @Column(name = "exercise_category", nullable = false, length = 50)
    private String exerciseCategory; // 운동 카테고리 (ex: 가슴, 등, 어깨, 하체, 복근, 유산소)

    @Column(name = "name", nullable = false, length = 100)
    private String name; // 운동 이름

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 운동 설명

    @Column(name = "use_yn", nullable = false)
    @Builder.Default
    private Boolean useYn = true; // 사용여부

    /**
     * 운동 종목이 활성화되어 있는지 확인
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(useYn);
    }

    /**
     * 운동 종목 비활성화
     */
    public void deactivate() {
        this.useYn = false;
    }

    /**
     * 운동 종목 활성화
     */
    public void activate() {
        this.useYn = true;
    }
}