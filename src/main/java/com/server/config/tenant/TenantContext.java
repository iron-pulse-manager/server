package com.gymmanager.config.tenant;

import com.gymmanager.common.exception.TenantException;
import lombok.extern.slf4j.Slf4j;

/**
 * Multi-tenant 컨텍스트 관리 클래스
 * 
 * 현재 요청의 사업장(Tenant) 정보를 ThreadLocal로 관리합니다.
 * 각 HTTP 요청마다 독립적인 사업장 컨텍스트를 유지합니다.
 */
@Slf4j
public class TenantContext {

    private static final ThreadLocal<Long> currentBusinessId = new ThreadLocal<>();
    private static final ThreadLocal<String> currentBusinessName = new ThreadLocal<>();

    /**
     * 현재 사업장 ID 설정
     */
    public static void setCurrentBusinessId(Long businessId) {
        if (businessId == null) {
            log.warn("Attempting to set null business ID");
            throw new IllegalArgumentException("Business ID cannot be null");
        }
        
        log.debug("Setting current business ID: {}", businessId);
        currentBusinessId.set(businessId);
    }

    /**
     * 현재 사업장 ID 조회
     */
    public static Long getCurrentBusinessId() {
        Long businessId = currentBusinessId.get();
        if (businessId == null) {
            log.error("Business ID not found in current context");
            throw TenantException.contextNotSet();
        }
        return businessId;
    }

    /**
     * 현재 사업장 ID 조회 (안전한 방식)
     * @return 사업장 ID 또는 null
     */
    public static Long getCurrentBusinessIdSafely() {
        return currentBusinessId.get();
    }

    /**
     * 현재 사업장 이름 설정
     */
    public static void setCurrentBusinessName(String businessName) {
        log.debug("Setting current business name: {}", businessName);
        currentBusinessName.set(businessName);
    }

    /**
     * 현재 사업장 이름 조회
     */
    public static String getCurrentBusinessName() {
        return currentBusinessName.get();
    }

    /**
     * 컨텍스트 정리
     * 요청 처리 완료 후 반드시 호출해야 합니다.
     */
    public static void clear() {
        log.debug("Clearing tenant context for business ID: {}", currentBusinessId.get());
        currentBusinessId.remove();
        currentBusinessName.remove();
    }

    /**
     * 현재 컨텍스트에 사업장 정보가 설정되어 있는지 확인
     */
    public static boolean isContextSet() {
        return currentBusinessId.get() != null;
    }

    /**
     * 디버깅용 컨텍스트 정보 출력
     */
    public static String getContextInfo() {
        return String.format("BusinessId: %s, BusinessName: %s", 
                currentBusinessId.get(), 
                currentBusinessName.get());
    }
}