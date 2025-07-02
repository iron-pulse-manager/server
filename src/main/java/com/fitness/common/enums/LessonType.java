package com.fitness.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 레슨 유형 Enum
 */
@Getter
@AllArgsConstructor
public enum LessonType {
    PERSONAL_PT("1:1 PT"),
    GROUP_PT("그룹 PT"),
    ONLINE_PT("온라인 PT"),
    PERSONAL_PILATES("필라테스 개인레슨"),
    GROUP_PILATES("필라테스 그룹레슨"),
    YOGA("요가"),
    SPINNING("스피닝"),
    CROSSFIT("크로스핏");

    private final String description;

}