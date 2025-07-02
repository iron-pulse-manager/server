package com.fitness.domain.product.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductType {
    
    /**
     * 회원권 - 헬스, 필라테스 등 일반 회원권
     * 주로 기간제 (valid_days 사용)
     */
    MEMBERSHIP("회원권"),
    
    /**
     * 개인레슨 - PT, 개인 필라테스 등
     * 주로 횟수제 (usage_count 사용)
     */
    PERSONAL_TRAINING("개인레슨"),
    
    /**
     * 락커 이용권 - 락커 사용 권한
     * 주로 기간제 (valid_days 사용)
     */
    LOCKER("락커"),
    
    /**
     * 기타 상품 - 프로틴, 운동용품, 샤워용품 등
     * 기간제 또는 횟수제 모두 가능
     */
    OTHERS("기타");

    private final String description;
}