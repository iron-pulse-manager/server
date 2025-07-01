package com.fitness.domain.member.entity;

/**
 * 사업장-회원 관계 상태 열거형
 */
public enum BusinessMemberStatus {
    
    /**
     * 활성 - 정상적으로 서비스 이용 중인 회원
     */
    ACTIVE("활성"),
    
    /**
     * 비활성 - 일시적으로 서비스 이용을 중단한 회원
     */
    INACTIVE("비활성"),
    
    /**
     * 정지 - 규정 위반 등으로 서비스 이용이 정지된 회원
     */
    SUSPENDED("정지"),
    
    /**
     * 만료 - 회원권이 만료된 회원
     */
    EXPIRED("만료");

    private final String description;

    BusinessMemberStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 활성 상태 여부 확인
     */
    public boolean isActive() {
        return this == ACTIVE;
    }

    /**
     * 서비스 이용 가능 상태 여부 확인
     */
    public boolean canUseService() {
        return this == ACTIVE;
    }

    /**
     * 만료 임박 상태 여부 확인 (추후 확장 가능)
     */
    public boolean isExpiringSoon() {
        // 추후 만료 임박 로직 추가 가능
        return false;
    }
}