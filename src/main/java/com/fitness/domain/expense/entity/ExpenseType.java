package com.fitness.domain.expense.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 지출 유형 Enum
 */
@Getter
@AllArgsConstructor
public enum ExpenseType {
    SALARY("인건비"),
    RENT("임대료"),
    UTILITIES("수도/경비"),
    OTHER("기타 경비");

    private final String description;
}