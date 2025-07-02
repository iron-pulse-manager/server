package com.fitness.domain.workout.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.user.entity.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writerId; // 작성자 ID

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
}