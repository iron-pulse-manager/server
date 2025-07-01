package com.fitness.domain.product.entity;

/**
 * 상품 유형 열거형
 */
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

    ProductType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 기본적으로 기간제를 사용하는 상품 유형인지 확인
     */
    public boolean isDefaultPeriodBased() {
        return this == MEMBERSHIP || this == LOCKER;
    }

    /**
     * 기본적으로 횟수제를 사용하는 상품 유형인지 확인
     */
    public boolean isDefaultCountBased() {
        return this == PERSONAL_TRAINING;
    }

    /**
     * 기간제와 횟수제 모두 사용 가능한 상품 유형인지 확인
     */
    public boolean isFlexibleType() {
        return this == OTHERS;
    }
}