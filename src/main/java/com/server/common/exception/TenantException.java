package com.gymmanager.common.exception;

/**
 * Multi-tenant 관련 예외 클래스
 * 
 * 사업장(Tenant) 관련 오류 시 발생하는 예외입니다.
 * 예: 사업장 정보 없음, 접근 권한 없음 등
 */
public class TenantException extends BusinessException {

    public TenantException(String message) {
        super(message, "TENANT_ERROR");
    }

    public TenantException(String message, String errorCode) {
        super(message, errorCode);
    }

    public TenantException(String message, Object errorDetails) {
        super(message, "TENANT_ERROR", errorDetails);
    }

    public TenantException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 사업장 정보를 찾을 수 없을 때 발생하는 예외
     */
    public static TenantException notFound() {
        return new TenantException("사업장 정보를 찾을 수 없습니다.", "TENANT_NOT_FOUND");
    }

    /**
     * 사업장 접근 권한이 없을 때 발생하는 예외
     */
    public static TenantException accessDenied() {
        return new TenantException("해당 사업장에 접근할 권한이 없습니다.", "TENANT_ACCESS_DENIED");
    }

    /**
     * 사업장 컨텍스트가 설정되지 않았을 때 발생하는 예외
     */
    public static TenantException contextNotSet() {
        return new TenantException("사업장 컨텍스트가 설정되지 않았습니다.", "TENANT_CONTEXT_NOT_SET");
    }
}