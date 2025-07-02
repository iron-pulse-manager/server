package com.fitness.domain.lesson.entity;

import lombok.Getter;

/**
 * 참여자 상태 Enum
 */
@Getter
public enum ParticipantStatus {
    PENDING("승인대기"),
    APPROVED("승인완료"),
    ATTENDED("출석"),
    ABSENT("미출석"),
    CANCELLED("취소");

    private final String description;

    ParticipantStatus(String description) {
        this.description = description;
    }

}