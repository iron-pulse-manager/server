package com.fitness.domain.workout.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.schedule.entity.LessonSchedule;
import com.fitness.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 운동일지 엔티티
 * 레슨 스케줄과 연결된 운동일지 정보를 관리
 */
@Entity
@Table(name = "workout_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutSession extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workout_session_id")
    private Long workoutSessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_schedule_id")
    private LessonSchedule lessonSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo; // 세션 메모

    // 연관관계 매핑
    @OneToMany(mappedBy = "workoutSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<WorkoutExercise> exercises = new ArrayList<>();

    @OneToMany(mappedBy = "workoutSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<WorkoutComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "workoutSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<WorkoutReply> replies = new ArrayList<>();

    // 편의 메서드
    public void addExercise(WorkoutExercise exercise) {
        exercises.add(exercise);
        exercise.setWorkoutSession(this);
    }

    public void removeExercise(WorkoutExercise exercise) {
        exercises.remove(exercise);
        exercise.setWorkoutSession(null);
    }

    public void addComment(WorkoutComment comment) {
        comments.add(comment);
        comment.setWorkoutSession(this);
    }

    public void removeComment(WorkoutComment comment) {
        comments.remove(comment);
        comment.setWorkoutSession(null);
    }

    public void addReply(WorkoutReply reply) {
        replies.add(reply);
        reply.setWorkoutSession(this);
    }

    public void removeReply(WorkoutReply reply) {
        replies.remove(reply);
        reply.setWorkoutSession(null);
    }

}