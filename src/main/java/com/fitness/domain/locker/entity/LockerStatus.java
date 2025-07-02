package com.fitness.domain.locker.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 락커 상태 Enum
 */
@Getter
@AllArgsConstructor
public enum LockerStatus {
    AVAILABLE("이용가능"),
    IN_USE("이용중"),
    EXPIRED("만료"),
    MAINTENANCE("점검중"),
    OUT_OF_ORDER("고장");

    private final String description;

}