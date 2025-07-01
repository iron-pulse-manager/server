package com.fitness.domain.business.entity;

/**
 * 사업장 소속 직원 상태 열거형
 * 데이터베이스 스키마의 status 컬럼 값과 일치
 */
public enum BusinessEmployeeStatus {
    
    /**
     * 승인 대기 - 직원이 사업장 연결을 신청한 상태
     */
    PENDING("승인대기"),
    
    /**
     * 승인 완료 - 사업장에서 승인하여 현재 근무중인 상태
     */
    APPROVED("승인완료"),
    
    /**
     * 승인 거절 - 사업장에서 가입 신청을 거절한 상태
     */
    REJECTED("거절"),
    
    /**
     * 정상 근무 - 정상적으로 근무중인 상태
     */
    NORMAL("정상"),
    
    /**
     * 휴직 - 휴직 상태
     */
    LEAVE("휴직"),
    
    /**
     * 퇴사 - 사업장에서 퇴사한 상태
     */
    RESIGNED("퇴사");

    private final String description;

    BusinessEmployeeStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}