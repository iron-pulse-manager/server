package com.fitness.domain.payment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 결제 상태 Enum
 */
@Getter
@AllArgsConstructor
public enum PaymentStatus {
    PENDING("대기"),
    COMPLETED("완료"),
    CANCELLED("취소");

    private final String description;

}