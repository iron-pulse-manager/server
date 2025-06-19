package com.gymmanager.config.security;

import com.gymmanager.common.enums.ClientType;
import com.gymmanager.common.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 인증 컨텍스트 클래스
 * 
 * 현재 요청의 인증 정보를 ThreadLocal로 관리합니다.
 * 클라이언트 타입, 사용자 역할, 권한 등을 포함합니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationContext {
    
    private static final ThreadLocal<AuthenticationContext> context = new ThreadLocal<>();

    /**
     * 현재 사용자 ID
     */
    private Long userId;

    /**
     * 현재 사업장 ID (Multi-tenant)
     */
    private Long businessId;

    /**
     * 클라이언트 타입 (WEB_ADMIN, STAFF_APP, MEMBER_APP)
     */
    private ClientType clientType;

    /**
     * 사용자 역할 (BUSINESS_OWNER, STAFF, MEMBER)
     */
    private UserRole userRole;

    /**
     * 사용자 권한 목록
     */
    private Set<UserRole.Permission> permissions;

    /**
     * 사용자 이름
     */
    private String userName;

    /**
     * JWT 토큰
     */
    private String token;

    /**
     * 현재 컨텍스트 설정
     */
    public static void setCurrent(AuthenticationContext authContext) {
        context.set(authContext);
    }

    /**
     * 현재 컨텍스트 조회
     */
    public static AuthenticationContext getCurrent() {
        return context.get();
    }

    /**
     * 컨텍스트 정리
     */
    public static void clear() {
        context.remove();
    }

    /**
     * 현재 사용자 ID 조회
     */
    public static Long getCurrentUserId() {
        AuthenticationContext current = getCurrent();
        return current != null ? current.getUserId() : null;
    }

    /**
     * 현재 사업장 ID 조회
     */
    public static Long getCurrentBusinessId() {
        AuthenticationContext current = getCurrent();
        return current != null ? current.getBusinessId() : null;
    }

    /**
     * 현재 클라이언트 타입 조회
     */
    public static ClientType getCurrentClientType() {
        AuthenticationContext current = getCurrent();
        return current != null ? current.getClientType() : null;
    }

    /**
     * 현재 사용자 역할 조회
     */
    public static UserRole getCurrentUserRole() {
        AuthenticationContext current = getCurrent();
        return current != null ? current.getUserRole() : null;
    }

    /**
     * 인증된 사용자인지 확인
     */
    public static boolean isAuthenticated() {
        return getCurrent() != null && getCurrentUserId() != null;
    }

    /**
     * 특정 권한을 가지고 있는지 확인
     */
    public boolean hasPermission(UserRole.Permission permission) {
        return permissions != null && permissions.contains(permission);
    }

    /**
     * 관리자인지 확인
     */
    public boolean isAdmin() {
        return userRole != null && userRole.isAdmin();
    }

    /**
     * 직원인지 확인
     */
    public boolean isStaff() {
        return userRole != null && userRole.isStaff();
    }

    /**
     * 회원인지 확인
     */
    public boolean isMember() {
        return userRole != null && userRole.isMember();
    }

    /**
     * 웹 클라이언트인지 확인
     */
    public boolean isWebClient() {
        return clientType != null && clientType.isWebClient();
    }

    /**
     * 모바일 앱인지 확인
     */
    public boolean isMobileApp() {
        return clientType != null && clientType.isMobileApp();
    }

    /**
     * 직원 앱인지 확인
     */
    public boolean isStaffApp() {
        return ClientType.STAFF_APP.equals(clientType);
    }

    /**
     * 회원 앱인지 확인
     */
    public boolean isMemberApp() {
        return ClientType.MEMBER_APP.equals(clientType);
    }

    @Override
    public String toString() {
        return "AuthenticationContext{" +
                "userId=" + userId +
                ", businessId=" + businessId +
                ", clientType=" + clientType +
                ", userRole=" + userRole +
                ", userName='" + userName + '\'' +
                '}';
    }
}