package com.fitness.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 성별 Enum
 */
@Getter
@AllArgsConstructor
public enum Gender {
    MALE("남성"),
    FEMALE("여성");

    private final String description;

}