package com.fitness.domain.workout.entity;

import com.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 운동일지 댓글 엔티티 (최상위 댓글만)
 * 운동 세션에 대한 회원과 트레이너의 최상위 댓글을 관리
 */
@Entity
@Table(name = "workout_comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workout_comment_id")
    private Long workoutCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_session_id", nullable = false)
    private WorkoutSession workoutSession;

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

    // 연관관계 매핑 - 대댓글들
    @OneToMany(mappedBy = "workoutComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    @Builder.Default
    private List<WorkoutReply> replies = new ArrayList<>();

    // 편의 메서드
    public void addReply(WorkoutReply reply) {
        replies.add(reply);
        reply.setWorkoutComment(this);
    }

    public void removeReply(WorkoutReply reply) {
        replies.remove(reply);
        reply.setWorkoutComment(null);
    }

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
     * 삭제된 댓글의 표시 내용
     */
    public String getDisplayContent() {
        if (delYn) {
            return "삭제된 댓글입니다.";
        }
        return content;
    }

    /**
     * 대댓글이 있는지 확인
     */
    public boolean hasReplies() {
        return replies != null && !replies.isEmpty();
    }
}