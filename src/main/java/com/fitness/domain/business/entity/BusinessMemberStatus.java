package com.fitness.domain.business.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessMemberStatus {
    ACTIVE("활성"),
    EXPIRING_SOON("만료임박"),
    EXPIRED("만료"),
    SUSPENDED("정지");

    private final String description;

}
