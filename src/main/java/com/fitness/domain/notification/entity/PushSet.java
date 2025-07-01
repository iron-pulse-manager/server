package com.fitness.domain.notification.entity;

import com.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 푸시 설정 엔티티
 * 시스템에서 사용할 수 있는 푸시 알림 유형들을 관리
 */
@Entity
@Table(name = "push_set")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PushSet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "push_id")
    private Long pushId;

    @Enumerated(EnumType.STRING)
    @Column(name = "push_type", nullable = false)
    private PushType pushType; // 푸시 유형

    @Column(name = "title", nullable = false, length = 100)
    private String title; // 푸시 제목

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 푸시 상세 설명

    @Column(name = "use_yn", nullable = false)
    @Builder.Default
    private Boolean useYn = true; // 사용여부

    @Column(name = "sort_order", nullable = false)
    @Builder.Default
    private Integer sortOrder = 0; // 푸시설정 순서

    /**
     * 푸시 유형
     */
    public enum PushType {
        WORKOUT("운동일지"),
        DIET("식단기록"),
        LESSON("레슨"),
        SCHEDULE("일정"),
        COMMUNITY("커뮤니티"),
        PAYMENT("결제"),
        MEMBERSHIP("회원권"),
        SYSTEM("시스템");

        private final String description;

        PushType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // 비즈니스 메서드

    /**
     * 푸시 설정 활성화
     */
    public void activate() {
        this.useYn = true;
    }

    /**
     * 푸시 설정 비활성화
     */
    public void deactivate() {
        this.useYn = false;
    }

    /**
     * 푸시 설정이 활성화되어 있는지 확인
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(useYn);
    }

    /**
     * 푸시 설정 정보 업데이트
     */
    public void updatePushInfo(String title, String description, Integer sortOrder) {
        this.title = title;
        this.description = description;
        this.sortOrder = sortOrder;
    }

    /**
     * 푸시 설정 요약 정보
     */
    public String getPushSummary() {
        return String.format("[%s] %s - %s", 
                pushType.getDescription(), 
                title, 
                isActive() ? "활성" : "비활성");
    }
}