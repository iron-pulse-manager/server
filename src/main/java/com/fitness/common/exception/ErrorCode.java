package com.fitness.common.exception;

/**
 * 에러 코드 정의
 */
public enum ErrorCode {
    
    // Common
    INVALID_INPUT("COMMON001", "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR("COMMON002", "서버 내부 오류가 발생했습니다."),
    
    // Owner
    OWNER_NOT_FOUND("OWNER001", "사장님 정보를 찾을 수 없습니다."),
    INVALID_LOGIN_CREDENTIALS("OWNER002", "로그인 정보가 올바르지 않습니다."),
    
    // Employee  
    EMPLOYEE_NOT_FOUND("EMPLOYEE001", "직원 정보를 찾을 수 없습니다."),
    EMPLOYEE_ALREADY_APPROVED("EMPLOYEE002", "이미 승인된 직원입니다."),
    
    // Member
    MEMBER_NOT_FOUND("MEMBER001", "회원 정보를 찾을 수 없습니다."),
    MEMBER_ALREADY_EXISTS("MEMBER002", "이미 가입된 회원입니다."),
    
    // Business
    BUSINESS_NOT_FOUND("BUSINESS001", "사업장 정보를 찾을 수 없습니다."),
    BUSINESS_NOT_APPROVED("BUSINESS002", "승인되지 않은 사업장입니다."),
    
    // Product
    PRODUCT_NOT_FOUND("PRODUCT001", "상품 정보를 찾을 수 없습니다."),
    PRODUCT_NAME_DUPLICATE("PRODUCT002", "이미 등록된 상품명입니다."),
    
    // Payment
    PAYMENT_NOT_FOUND("PAYMENT001", "결제 정보를 찾을 수 없습니다."),
    INSUFFICIENT_AMOUNT("PAYMENT002", "결제 금액이 부족합니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}