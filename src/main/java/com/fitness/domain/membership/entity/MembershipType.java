package com.fitness.domain.membership.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 이용권 유형 Enum
 */
@Getter
@AllArgsConstructor
public enum MembershipType {
    GYM("회원권"),
    PT("PT"),
    PILATES("필라테스");

    private final String description;

}