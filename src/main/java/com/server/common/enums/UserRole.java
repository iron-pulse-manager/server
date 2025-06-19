package com.gymmanager.common.enums;

import java.util.Set;

/**
 * 사용자 역할 열거형
 * 
 * 시스템 내에서 사용자의 역할과 권한을 정의합니다.
 * 각 역할별로 접근 가능한 기능과 데이터가 다릅니다.
 */
public enum UserRole {
    /**
     * 사업장 사장님
     * - 최고 권한
     * - 모든 사업장 데이터 접근 가능
     * - 직원 관리, 회원 관리, 매출 통계 등
     */
    BUSINESS_OWNER("사업장 사장님", Set.of(
        Permission.MANAGE_BUSINESS,
        Permission.MANAGE_STAFF,
        Permission.MANAGE_MEMBERS,
        Permission.VIEW_ALL_SALES,
        Permission.MANAGE_PRODUCTS,
        Permission.VIEW_STATISTICS,
        Permission.SEND_SMS,
        Permission.MANAGE_SCHEDULE
    )),
    
    /**
     * 직원 (트레이너, 직원)
     * - 제한된 권한
     * - 담당 회원 관리
     * - 개인 매출 조회
     */
    STAFF("직원", Set.of(
        Permission.VIEW_ASSIGNED_MEMBERS,
        Permission.UPDATE_MEMBER_INFO,
        Permission.VIEW_OWN_SALES,
        Permission.VIEW_OWN_SCHEDULE,
        Permission.UPDATE_ATTENDANCE
    )),
    
    /**
     * 일반 회원
     * - 최소 권한
     * - 개인 정보만 접근 가능
     */
    MEMBER("회원", Set.of(
        Permission.VIEW_OWN_INFO,
        Permission.UPDATE_OWN_INFO,
        Permission.VIEW_OWN_ATTENDANCE,
        Permission.VIEW_OWN_SUBSCRIPTION
    ));

    private final String description;
    private final Set<Permission> permissions;

    UserRole(String description, Set<Permission> permissions) {
        this.description = description;
        this.permissions = permissions;
    }

    public String getDescription() {
        return description;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    /**
     * 특정 권한을 가지고 있는지 확인
     */
    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    /**
     * 관리자 역할인지 확인
     */
    public boolean isAdmin() {
        return this == BUSINESS_OWNER;
    }

    /**
     * 직원 역할인지 확인
     */
    public boolean isStaff() {
        return this == STAFF;
    }

    /**
     * 회원 역할인지 확인
     */
    public boolean isMember() {
        return this == MEMBER;
    }

    /**
     * 권한 열거형
     */
    public enum Permission {
        // 사업장 관리
        MANAGE_BUSINESS,
        
        // 직원 관리
        MANAGE_STAFF,
        
        // 회원 관리
        MANAGE_MEMBERS,
        VIEW_ASSIGNED_MEMBERS,
        UPDATE_MEMBER_INFO,
        
        // 매출 관리
        VIEW_ALL_SALES,
        VIEW_OWN_SALES,
        
        // 상품 관리
        MANAGE_PRODUCTS,
        
        // 통계 조회
        VIEW_STATISTICS,
        
        // 문자 발송
        SEND_SMS,
        
        // 일정 관리
        MANAGE_SCHEDULE,
        VIEW_OWN_SCHEDULE,
        
        // 출석 관리
        UPDATE_ATTENDANCE,
        VIEW_OWN_ATTENDANCE,
        
        // 개인 정보
        VIEW_OWN_INFO,
        UPDATE_OWN_INFO,
        VIEW_OWN_SUBSCRIPTION
    }
}