package com.gymmanager.common.enums;

/**
 * 클라이언트 타입 열거형
 * 
 * API를 호출하는 클라이언트의 종류를 구분합니다.
 * 각 클라이언트 타입별로 다른 인증 방식과 권한을 적용합니다.
 */
public enum ClientType {
    /**
     * 웹 관리자 (사장님용 웹사이트)
     * - 전체 사업장 관리 기능
     * - 회원 관리, 통계, 매출 관리 등
     */
    WEB_ADMIN("웹 관리자", "web"),
    
    /**
     * 직원 앱 (트레이너, 직원용 모바일 앱)
     * - 담당 회원 관리
     * - 개인 매출 조회
     * - 일정 관리 등
     */
    STAFF_APP("직원 앱", "staff"),
    
    /**
     * 회원 앱 (일반 회원용 모바일 앱)
     * - 개인 정보 관리
     * - 출석 체크
     * - 예약 시스템 등
     */
    MEMBER_APP("회원 앱", "member");

    private final String description;
    private final String apiPrefix;

    ClientType(String description, String apiPrefix) {
        this.description = description;
        this.apiPrefix = apiPrefix;
    }

    public String getDescription() {
        return description;
    }

    public String getApiPrefix() {
        return apiPrefix;
    }

    /**
     * API 경로 prefix로부터 클라이언트 타입 찾기
     */
    public static ClientType fromApiPrefix(String prefix) {
        for (ClientType type : values()) {
            if (type.apiPrefix.equals(prefix)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown API prefix: " + prefix);
    }

    /**
     * 모바일 앱인지 확인
     */
    public boolean isMobileApp() {
        return this == STAFF_APP || this == MEMBER_APP;
    }

    /**
     * 웹 클라이언트인지 확인
     */
    public boolean isWebClient() {
        return this == WEB_ADMIN;
    }
}