package com.fitness.domain.workout.entity;

import com.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 운동일지 대댓글 엔티티
 * 운동일지 댓글에 대한 대댓글을 관리
 */
@Entity
@Table(name = "workout_replies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workout_reply_id")
    private Long workoutReplyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_comment_id", nullable = false)
    private WorkoutComment workoutComment;

    @Column(name = "writer_id", nullable = false)
    private Long writerId; // 작성자 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "writer_type", nullable = false)
    private WriterType writerType; // 작성자 유형

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "del_yn", nullable = false)
    @Builder.Default
    private Boolean delYn = false; // 삭제 여부

    /**
     * 댓글 작성자가 회원인지 확인
     */
    public boolean isWrittenByMember() {
        return WriterType.회원.equals(writerType);
    }

    /**
     * 댓글 작성자가 트레이너인지 확인
     */
    public boolean isWrittenByTrainer() {
        return WriterType.강사.equals(writerType);
    }

    /**
     * 작성자 유형
     */
    public enum WriterType {
        강사("강사"),
        회원("회원");

        private final String description;

        WriterType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 삭제된 대댓글의 표시 내용
     */
    public String getDisplayContent() {
        if (delYn) {
            return "삭제된 댓글입니다.";
        }
        return content;
    }

    /**
     * 대댓글이 최상위 댓글 작성자에게 작성된 것인지 확인
     */
    public boolean isReplyToOriginalCommenter() {
        if (workoutComment == null) {
            return false;
        }
        
        // 둘 다 같은 타입이면서 같은 사람인지 확인
        if (this.writerType.equals(workoutComment.getWriterType())) {
            return !this.writerId.equals(workoutComment.getWriterId());
        }
        
        // 서로 다른 타입이면 다른 사람
        return true;
    }
}