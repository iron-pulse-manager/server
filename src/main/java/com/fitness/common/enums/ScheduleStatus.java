package com.fitness.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 스케줄 상태 Enum
 */
@Getter
@AllArgsConstructor
public enum ScheduleStatus {
    SCHEDULED("진행예정"),
    IN_PROGRESS("진행중"),
    COMPLETED("완료"),
    CANCELLED("취소");

    private final String description;

}