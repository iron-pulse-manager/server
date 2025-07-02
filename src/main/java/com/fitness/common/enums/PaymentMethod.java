package com.fitness.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 결제 방법 Enum
 */
@Getter
@AllArgsConstructor
public enum PaymentMethod {
    CARD("카드"),
    CASH("현금"),
    BANK_TRANSFER("계좌이체");

    private final String description;

}