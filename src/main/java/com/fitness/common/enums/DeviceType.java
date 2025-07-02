package com.fitness.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 디바이스 유형 Enum
 */
@Getter
@AllArgsConstructor
public enum DeviceType {
    WEB("웹"),
    APP("앱");

    private final String description;

}