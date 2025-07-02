package com.fitness.domain.workout.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 첨부파일 타입 Enum
 */
@Getter
@AllArgsConstructor
public enum AttachType {
    IMAGE("사진"),
    VIDEO("동영상");

    private final String description;

}