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

    @Enumerated(EnumType.STRING)
    @Column(name = "exercise_category", nullable = false)
    private ExerciseCategory exerciseCategory; // 운동 카테고리

    @Column(name = "name", nullable = false, length = 100)
    private String name; // 운동 이름

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 운동 설명

    @Column(name = "use_yn", nullable = false)
    @Builder.Default
    private boolean useYn = true; // 사용여부

}