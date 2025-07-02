package com.fitness.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 사용자 상태 Enum
 */
@Getter
@AllArgsConstructor
public enum UserStatus {
    ACTIVE("활성"),
    INACTIVE("비활성");

    private final String description;

}