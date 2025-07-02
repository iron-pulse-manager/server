package com.fitness.domain.sms.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 발송 상태 Enum
 */
@Getter
@AllArgsConstructor
public enum SendStatus {
    SUCCESS("발송완료"),
    FAILED("실패");

    private final String description;

}