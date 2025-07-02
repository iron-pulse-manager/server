package com.fitness.domain.business.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessStatus {
    ACTIVE("활성"),
    INACTIVE("비활성"),
    PENDING("대기"),
    REJECTED("승인거절");

    private final String description;

}
