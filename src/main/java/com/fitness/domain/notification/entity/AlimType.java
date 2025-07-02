package com.fitness.domain.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 알림 타입 Enum
 */
@Getter
@AllArgsConstructor
public enum AlimType {
    LESSON_REQUEST("레슨 신청"),
    LESSON_APPROVED("레슨 승인"),
    LESSON_REJECTED("레슨 거절"),
    LESSON_CANCELLED("레슨 취소"),
    WORKOUT_COMMENT("운동 댓글"),
    WORKOUT_REPLY("운동 댓글 답글"),
    COMMUNITY_COMMENT("커뮤니티 댓글"),
    COMMUNITY_REPLY("커뮤니티 답글"),
    SCHEDULE_PARTICIPANT("일정 참여자 등록"),
    MEMBERSHIP_EXPIRING("회원권 만료 임박"),
    PAYMENT_COMPLETED("결제 완료"),
    OUTSTANDING_PAYMENT("미수금 결제"),
    SYSTEM_NOTICE("시스템 공지");

    private final String description;

}