package com.fitness.domain.lesson.entity;

import lombok.Getter;

/**
 * 레슨 타입 Enum
 */
@Getter
public enum LessonType {
    PERSONAL_PT("개인 PT"),
    GROUP_PT("그룹 PT"),
    ONLINE_PT("온라인 PT"),
    PERSONAL_PILATES("개인 필라테스"),
    GROUP_PILATES("그룹 필라테스"),
    YOGA("요가"),
    SPINNING("스피닝"),
    CROSSFIT("크로스핏");

    private final String description;

    LessonType(String description) {
        this.description = description;
    }

}