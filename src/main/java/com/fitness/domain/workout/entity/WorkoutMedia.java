package com.fitness.domain.workout.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.employee.entity.BusinessEmployee;
import jakarta.persistence.*;
import lombok.*;

/**
 * 운동일지 미디어 파일 엔티티
 * 운동 세션 또는 특정 운동에 첨부된 사진/동영상을 관리
 */
@Entity
@Table(name = "workout_media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutMedia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id")
    private Long mediaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private WorkoutSession session; // 세션 전체에 연결된 미디어

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_log_id")
    private WorkoutExercise exercise; // 특정 운동에 연결된 미디어

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", nullable = false)
    private MediaType mediaType;

    @Column(name = "media_url", nullable = false, length = 500)
    private String mediaUrl;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl; // 비디오 썸네일

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "media_order")
    @Builder.Default
    private Integer mediaOrder = 1;

    @Column(name = "caption", columnDefinition = "TEXT")
    private String caption; // 미디어 설명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private BusinessEmployee uploadedBy; // 업로드한 트레이너

    /**
     * 미디어 타입
     */
    public enum MediaType {
        IMAGE("이미지"),
        VIDEO("동영상");

        private final String description;

        MediaType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 파일 크기를 MB 단위로 반환
     */
    public Double getFileSizeMB() {
        if (fileSizeBytes != null) {
            return fileSizeBytes / (1024.0 * 1024.0);
        }
        return null;
    }

    /**
     * 미디어가 세션 전체에 연결되어 있는지 확인
     */
    public boolean isSessionMedia() {
        return session != null && exercise == null;
    }

    /**
     * 미디어가 특정 운동에 연결되어 있는지 확인
     */
    public boolean isExerciseMedia() {
        return exercise != null;
    }
}