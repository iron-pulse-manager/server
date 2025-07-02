package com.fitness.domain.workout.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.workout.entity.AttachType;
import jakarta.persistence.*;
import lombok.*;

/**
 * 운동 종목 첨부파일 엔티티
 * 운동 종목별 사진/동영상 첨부파일을 관리
 */
@Entity
@Table(name = "exercise_attach")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseAttach extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_attach_id")
    private Long exerciseAttachId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_exercise_id", nullable = false)
    private WorkoutExercise workoutExercise;

    @Column(name = "attach_url", nullable = false, length = 500)
    private String attachUrl; // 첨부파일 URL

    @Enumerated(EnumType.STRING)
    @Column(name = "attach_type", nullable = false)
    private AttachType attachType; // 첨부파일 유형

    @Column(name = "attach_order")
    @Builder.Default
    private Integer attachOrder = 1; // 첨부파일 순서

    @Column(name = "file_size")
    private Integer fileSize; // 파일 사이즈 (byte)

}