package com.fitness.domain.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 푸시 타입 Enum
 */
@Getter
@AllArgsConstructor
public enum PushType {
    WORKOUT("운동일지"),
    DIET("식단기록"),
    LESSON("레슨"),
    SCHEDULE("일정"),
    COMMUNITY("커뮤니티"),
    PAYMENT("결제"),
    MEMBERSHIP("회원권"),
    SYSTEM("시스템");

    private final String description;

}