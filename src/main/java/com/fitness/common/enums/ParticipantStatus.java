package com.fitness.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 레슨 참가 상태 Enum
 */
@Getter
@AllArgsConstructor
public enum ParticipantStatus {
    READY("승인대기"),
    APPROVED("승인완료"),
    ATTENDED("출석"),
    ABSENT("미출석"),
    CANCELLED("취소");

    private final String description;

}