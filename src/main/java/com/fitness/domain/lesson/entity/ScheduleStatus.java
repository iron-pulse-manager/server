package com.fitness.domain.lesson.entity;

import lombok.Getter;

/**
 * 일정 상태 Enum
 */
@Getter
public enum ScheduleStatus {
    SCHEDULED("진행예정"),
    IN_PROGRESS("진행중"),
    COMPLETED("완료"),
    CANCELLED("취소");

    private final String description;

    ScheduleStatus(String description) {
        this.description = description;
    }

}