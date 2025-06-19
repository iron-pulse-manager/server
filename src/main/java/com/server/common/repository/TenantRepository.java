package com.gymmanager.common.repository;

import com.gymmanager.common.entity.TenantBaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Multi-tenant 기반 Repository 인터페이스
 * 
 * 사업장별 데이터 격리를 자동으로 처리하는 Repository입니다.
 * 모든 쿼리에 businessId 조건이 자동으로 추가됩니다.
 * 
 * @param <T> 엔터티 타입 (TenantBaseEntity를 상속받은 클래스)
 * @param <ID> ID 타입
 */
@NoRepositoryBean
public interface TenantRepository<T extends TenantBaseEntity, ID> 
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    /**
     * 현재 사업장의 모든 엔터티 조회
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.businessId = :businessId")
    List<T> findAllByCurrentBusiness(@Param("businessId") Long businessId);

    /**
     * 현재 사업장의 엔터티 페이징 조회
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.businessId = :businessId")
    Page<T> findAllByCurrentBusiness(@Param("businessId") Long businessId, Pageable pageable);

    /**
     * 현재 사업장에서 ID로 엔터티 조회
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.id = :id AND e.businessId = :businessId")
    Optional<T> findByIdAndCurrentBusiness(@Param("id") ID id, @Param("businessId") Long businessId);

    /**
     * 현재 사업장에서 ID로 엔터티 존재 여부 확인
     */
    @Query("SELECT COUNT(e) > 0 FROM #{#entityName} e WHERE e.id = :id AND e.businessId = :businessId")
    boolean existsByIdAndCurrentBusiness(@Param("id") ID id, @Param("businessId") Long businessId);

    /**
     * 현재 사업장의 엔터티 개수 조회
     */
    @Query("SELECT COUNT(e) FROM #{#entityName} e WHERE e.businessId = :businessId")
    long countByCurrentBusiness(@Param("businessId") Long businessId);

    /**
     * 현재 사업장에서 ID로 엔터티 삭제
     */
    @Query("DELETE FROM #{#entityName} e WHERE e.id = :id AND e.businessId = :businessId")
    void deleteByIdAndCurrentBusiness(@Param("id") ID id, @Param("businessId") Long businessId);
}