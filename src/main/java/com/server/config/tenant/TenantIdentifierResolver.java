package com.gymmanager.config.tenant;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Hibernate Multi-tenant Identifier Resolver
 * 
 * Hibernate가 현재 요청의 tenant(사업장) ID를 식별할 수 있도록 도와주는 클래스입니다.
 * TenantContext에서 현재 사업장 ID를 가져와서 Hibernate에 제공합니다.
 */
@Component
@Slf4j
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver<String>, HibernatePropertiesCustomizer {

    private static final String DEFAULT_TENANT = "default";

    /**
     * 현재 요청의 tenant identifier를 반환합니다.
     * TenantContext에서 사업장 ID를 가져와서 문자열로 변환합니다.
     */
    @Override
    public String resolveCurrentTenantIdentifier() {
        try {
            Long businessId = TenantContext.getCurrentBusinessIdSafely();
            String tenantId = businessId != null ? businessId.toString() : DEFAULT_TENANT;
            
            log.debug("Resolved tenant identifier: {}", tenantId);
            return tenantId;
            
        } catch (Exception e) {
            log.warn("Failed to resolve tenant identifier, using default: {}", e.getMessage());
            return DEFAULT_TENANT;
        }
    }

    /**
     * tenant identifier의 유효성을 검증합니다.
     * 우리의 경우 모든 tenant ID가 유효하다고 가정합니다.
     */
    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

    /**
     * Hibernate 속성을 커스터마이징하여 이 resolver를 등록합니다.
     */
    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
        log.info("TenantIdentifierResolver registered with Hibernate");
    }
}