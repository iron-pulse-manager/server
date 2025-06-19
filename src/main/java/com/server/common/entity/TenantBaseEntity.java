package com.gymmanager.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Multi-tenant 엔터티의 기본 클래스
 * 
 * 사업장별 데이터 분리를 위해 businessId 필드를 포함합니다.
 * 모든 사업장 관련 엔터티는 이 클래스를 상속받아야 합니다.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class TenantBaseEntity extends BaseEntity {

    @Column(name = "business_id", nullable = false)
    private Long businessId;

    /**
     * 현재 사용자의 사업장 ID로 businessId를 자동 설정합니다.
     */
    @PrePersist
    public void prePersist() {
        if (this.businessId == null) {
            // TenantContext에서 현재 사업장 ID를 가져와서 설정
            this.businessId = getCurrentBusinessId();
        }
    }

    /**
     * 현재 컨텍스트의 사업장 ID를 가져옵니다.
     * TenantContext에서 현재 사업장 ID를 조회합니다.
     */
    private Long getCurrentBusinessId() {
        return com.gymmanager.config.tenant.TenantContext.getCurrentBusinessIdSafely();
    }
}