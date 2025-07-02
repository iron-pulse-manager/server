package com.fitness.domain.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuthType {
    MASTER("마스터"),
    ADMIN("관리자"),
    CS("CS직원");

    private final String description;
}
