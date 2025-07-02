package com.fitness.domain.sms.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 메시지 타입 Enum
 */
@Getter
@AllArgsConstructor
public enum MessageType {
    SINGLE("단건 문자"),
    BULK("단체 문자");

    private final String description;

}