package com.fitness.domain.device.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 디바이스 타입 Enum
 */
@Getter
@AllArgsConstructor
public enum DeviceType {
    IOS("iOS"),
    ANDROID("Android");

    private final String description;

}