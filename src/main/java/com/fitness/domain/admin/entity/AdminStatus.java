package com.fitness.domain.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 관리자 상태 Enum
 */
@Getter
@AllArgsConstructor
public enum AdminStatus {
    ACTIVE("활성"),
    INACTIVE("비활성"),
    SUSPENDED("정지");

    private final String description;

}