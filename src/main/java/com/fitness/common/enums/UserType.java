package com.fitness.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 사용자 유형 Enum
 */
@Getter
@AllArgsConstructor
public enum UserType {
    OWNER("사장"),
    EMPLOYEE("직원"),
    MEMBER("회원");

    private final String description;

}