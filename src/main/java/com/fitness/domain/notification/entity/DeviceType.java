package com.fitness.domain.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 디바이스 타입 Enum
 */
@Getter
@AllArgsConstructor
public enum DeviceType {
    WEB("웹"),
    APP("앱");

    private final String description;

}