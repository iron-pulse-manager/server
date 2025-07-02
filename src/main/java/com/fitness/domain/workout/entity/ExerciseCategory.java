package com.fitness.domain.workout.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 운동 카테고리 Enum
 */
@AllArgsConstructor
@Getter
public enum ExerciseCategory {
    CHEST("가슴"),
    BACK("등"),
    SHOULDER("어깨"),
    LEGS("하체"),
    ABS("복근"),
    CARDIO("유산소"),
    ARMS("팔"),
    CORE("코어"),
    FULL_BODY("전신"),
    STRETCHING("스트레칭");

    private final String description;
}