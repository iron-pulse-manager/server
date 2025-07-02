package com.fitness.domain.workout.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.user.entity.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writerId; // 작성자 ID

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    // 연관관계 매핑 - 대댓글들
    @OneToMany(mappedBy = "workoutComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    @Builder.Default
    private List<WorkoutReply> replies = new ArrayList<>();
}