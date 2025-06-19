package com.gymmanager.common.exception;

import lombok.Getter;

/**
 * 비즈니스 로직 예외 클래스
 * 
 * 비즈니스 규칙 위반이나 로직 오류 시 발생하는 예외입니다.
 * 에러 코드와 메시지를 포함하여 클라이언트에게 명확한 오류 정보를 제공합니다.
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 에러 코드
     */
    private final String errorCode;

    /**
     * 에러 상세 정보
     */
    private final Object errorDetails;

    public BusinessException(String message) {
        super(message);
        this.errorCode = "BUSINESS_ERROR";
        this.errorDetails = null;
    }

    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.errorDetails = null;
    }

    public BusinessException(String message, String errorCode, Object errorDetails) {
        super(message);
        this.errorCode = errorCode;
        this.errorDetails = errorDetails;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "BUSINESS_ERROR";
        this.errorDetails = null;
    }
}