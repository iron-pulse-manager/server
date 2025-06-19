package com.gymmanager.common.service;

import com.gymmanager.common.entity.TenantBaseEntity;
import com.gymmanager.common.exception.BusinessException;
import com.gymmanager.common.repository.TenantRepository;
import com.gymmanager.config.tenant.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Multi-tenant 기반 서비스 기본 클래스
 * 
 * 사업장별 데이터 격리를 보장하는 기본 CRUD 작업을 제공합니다.
 * 모든 작업은 현재 사업장 컨텍스트 내에서 수행됩니다.
 * 
 * @param <T> 엔터티 타입
 * @param <ID> ID 타입
 * @param <R> Repository 타입
 */
@Slf4j
@Transactional(readOnly = true)
public abstract class TenantService<T extends TenantBaseEntity, ID, R extends TenantRepository<T, ID>> {

    protected final R repository;

    protected TenantService(R repository) {
        this.repository = repository;
    }

    /**
     * 현재 사업장의 모든 엔터티 조회
     */
    public List<T> findAll() {
        Long businessId = getCurrentBusinessId();
        return repository.findAllByCurrentBusiness(businessId);
    }

    /**
     * 현재 사업장의 엔터티 페이징 조회
     */
    public Page<T> findAll(Pageable pageable) {
        Long businessId = getCurrentBusinessId();
        return repository.findAllByCurrentBusiness(businessId, pageable);
    }

    /**
     * 현재 사업장에서 ID로 엔터티 조회
     */
    public Optional<T> findById(ID id) {
        Long businessId = getCurrentBusinessId();
        return repository.findByIdAndCurrentBusiness(id, businessId);
    }

    /**
     * 현재 사업장에서 ID로 엔터티 조회 (필수)
     * 엔터티가 존재하지 않으면 예외 발생
     */
    public T findByIdOrThrow(ID id) {
        return findById(id)
                .orElseThrow(() -> new BusinessException(
                        getEntityName() + "을(를) 찾을 수 없습니다.", 
                        "ENTITY_NOT_FOUND"
                ));
    }

    /**
     * 현재 사업장에서 엔터티 존재 여부 확인
     */
    public boolean existsById(ID id) {
        Long businessId = getCurrentBusinessId();
        return repository.existsByIdAndCurrentBusiness(id, businessId);
    }

    /**
     * 현재 사업장의 엔터티 개수 조회
     */
    public long count() {
        Long businessId = getCurrentBusinessId();
        return repository.countByCurrentBusiness(businessId);
    }

    /**
     * 엔터티 저장
     * 사업장 ID가 자동으로 설정됩니다.
     */
    @Transactional
    public T save(T entity) {
        // 사업장 ID 설정 (TenantBaseEntity의 @PrePersist에서 자동 처리)
        Long businessId = getCurrentBusinessId();
        entity.setBusinessId(businessId);
        
        log.debug("Saving {} with business ID: {}", getEntityName(), businessId);
        return repository.save(entity);
    }

    /**
     * 엔터티 수정
     * 사업장 소유권을 확인한 후 수정합니다.
     */
    @Transactional
    public T update(ID id, T entity) {
        T existingEntity = findByIdOrThrow(id);
        
        // 업데이트할 엔터티의 ID와 사업장 ID 설정
        entity.setId(existingEntity.getId());
        entity.setBusinessId(existingEntity.getBusinessId());
        
        log.debug("Updating {} with ID: {} for business: {}", 
                getEntityName(), id, existingEntity.getBusinessId());
        
        return repository.save(entity);
    }

    /**
     * 현재 사업장에서 엔터티 삭제
     */
    @Transactional
    public void deleteById(ID id) {
        Long businessId = getCurrentBusinessId();
        
        // 먼저 엔터티 존재 여부 확인
        if (!repository.existsByIdAndCurrentBusiness(id, businessId)) {
            throw new BusinessException(
                    getEntityName() + "을(를) 찾을 수 없습니다.", 
                    "ENTITY_NOT_FOUND"
            );
        }
        
        log.debug("Deleting {} with ID: {} for business: {}", getEntityName(), id, businessId);
        repository.deleteByIdAndCurrentBusiness(id, businessId);
    }

    /**
     * 엔터티 삭제
     */
    @Transactional
    public void delete(T entity) {
        deleteById((ID) entity.getId());
    }

    /**
     * 현재 사업장 ID 조회
     */
    protected Long getCurrentBusinessId() {
        return TenantContext.getCurrentBusinessId();
    }

    /**
     * 엔터티 이름 반환 (로깅 및 에러 메시지용)
     * 하위 클래스에서 구현해야 합니다.
     */
    protected abstract String getEntityName();
}