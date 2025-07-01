package com.fitness.domain.workout.entity;

import com.fitness.common.BaseEntity;
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
    private Long fileSize; // 파일 사이즈 (byte)

    /**
     * 첨부파일 유형
     */
    public enum AttachType {
        IMAGE("사진"),
        VIDEO("동영상");

        private final String description;

        AttachType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 파일 크기를 KB 단위로 반환
     */
    public double getFileSizeInKB() {
        if (fileSize == null) {
            return 0.0;
        }
        return fileSize / 1024.0;
    }

    /**
     * 파일 크기를 MB 단위로 반환
     */
    public double getFileSizeInMB() {
        if (fileSize == null) {
            return 0.0;
        }
        return fileSize / (1024.0 * 1024.0);
    }

    /**
     * 이미지 파일인지 확인
     */
    public boolean isImage() {
        return AttachType.IMAGE.equals(attachType);
    }

    /**
     * 비디오 파일인지 확인
     */
    public boolean isVideo() {
        return AttachType.VIDEO.equals(attachType);
    }
}