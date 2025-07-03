package com.fitness.domain.product.repository;

import com.fitness.domain.product.entity.Product;
import com.fitness.domain.product.entity.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 상품 리포지토리
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> { // TODO: ProductRepositoryCustom 구현 후 재연결

    /**
     * 특정 사업장의 모든 상품 조회
     */
    List<Product> findByBusiness_BusinessId(Long businessId);

    /**
     * 특정 사업장의 활성 상품 조회
     */
    List<Product> findByBusiness_BusinessIdAndIsActive(Long businessId, Boolean isActive);

    /**
     * 특정 사업장의 활성 상품 조회 (페이징)
     */
    Page<Product> findByBusiness_BusinessIdAndIsActive(Long businessId, Boolean isActive, Pageable pageable);

    /**
     * 특정 사업장의 특정 유형 상품 조회
     */
    List<Product> findByBusiness_BusinessIdAndProductType(Long businessId, ProductType productType);

    /**
     * 특정 사업장의 특정 유형 활성 상품 조회
     */
    List<Product> findByBusiness_BusinessIdAndProductTypeAndIsActive(Long businessId, ProductType productType, Boolean isActive);

    /**
     * 상품명으로 검색 (특정 사업장)
     */
    List<Product> findByBusiness_BusinessIdAndNameContainingIgnoreCase(Long businessId, String productName);

    /**
     * 특정 사업장의 특정 상품명으로 정확 검색
     */
    Optional<Product> findByBusiness_BusinessIdAndName(Long businessId, String productName);

    /**
     * 특정 사업장에서 상품명 중복 확인
     */
    boolean existsByBusiness_BusinessIdAndName(Long businessId, String productName);

    /**
     * 특정 사업장에서 상품명 중복 확인 (자기 제외)
     */
    boolean existsByBusiness_BusinessIdAndNameAndProductIdNot(Long businessId, String productName, Long productId);

    /**
     * 가격 범위로 상품 검색
     */
    @Query("SELECT p FROM Product p WHERE p.business.businessId = :businessId " +
           "AND p.isActive = true " +
           "AND p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByBusinessIdAndPriceRange(@Param("businessId") Long businessId,
                                               @Param("minPrice") Long minPrice,
                                               @Param("maxPrice") Long maxPrice);

    /**
     * 기간제 상품 조회 (valid_days가 있는 상품)
     */
    @Query("SELECT p FROM Product p WHERE p.business.businessId = :businessId " +
           "AND p.isActive = true " +
           "AND p.validDay IS NOT NULL")
    List<Product> findPeriodBasedProducts(@Param("businessId") Long businessId);

    /**
     * 횟수제 상품 조회 (usage_count가 있는 상품)
     */
    @Query("SELECT p FROM Product p WHERE p.business.businessId = :businessId " +
           "AND p.isActive = true " +
           "AND p.usageCnt IS NOT NULL")
    List<Product> findCountBasedProducts(@Param("businessId") Long businessId);

    /**
     * 특정 기간 내 상품 조회 (valid_days 기준)
     */
    @Query("SELECT p FROM Product p WHERE p.business.businessId = :businessId " +
           "AND p.isActive = true " +
           "AND p.validDay BETWEEN :minDays AND :maxDays")
    List<Product> findByValidDaysRange(@Param("businessId") Long businessId,
                                      @Param("minDays") Integer minDays,
                                      @Param("maxDays") Integer maxDays);

    /**
     * 특정 횟수 범위 상품 조회 (usage_count 기준)
     */
    @Query("SELECT p FROM Product p WHERE p.business.businessId = :businessId " +
           "AND p.isActive = true " +
           "AND p.usageCnt BETWEEN :minCount AND :maxCount")
    List<Product> findByUsageCountRange(@Param("businessId") Long businessId,
                                       @Param("minCount") Integer minCount,
                                       @Param("maxCount") Integer maxCount);

    /**
     * 상품 유형별 개수 조회
     */
    @Query("SELECT p.productType, COUNT(p) FROM Product p " +
           "WHERE p.business.businessId = :businessId AND p.isActive = true " +
           "GROUP BY p.productType")
    List<Object[]> countByProductTypeAndBusinessId(@Param("businessId") Long businessId);

    /**
     * 인기 상품 조회 (결제 횟수 기준)
     */
    @Query("SELECT p FROM Product p " +
           "LEFT JOIN Payment pay ON pay.product = p " +
           "WHERE p.business.businessId = :businessId AND p.isActive = true " +
           "GROUP BY p.productId " +
           "ORDER BY COUNT(pay.paymentId) DESC")
    List<Product> findPopularProducts(@Param("businessId") Long businessId, Pageable pageable);

    /**
     * 최근 등록된 상품 조회
     */
    @Query("SELECT p FROM Product p WHERE p.business.businessId = :businessId " +
           "AND p.isActive = true " +
           "ORDER BY p.createdAt DESC")
    List<Product> findRecentProducts(@Param("businessId") Long businessId, Pageable pageable);

    /**
     * 특정 사업장의 상품 수 조회
     */
    Long countByBusiness_BusinessId(Long businessId);

    /**
     * 특정 사업장의 활성 상품 수 조회
     */
    Long countByBusiness_BusinessIdAndIsActive(Long businessId, Boolean isActive);

    /**
     * 특정 사업장의 특정 유형 상품 수 조회
     */
    Long countByBusiness_BusinessIdAndProductType(Long businessId, ProductType productType);

    /**
     * 가격대별 상품 분포 조회
     */
    @Query("SELECT " +
           "CASE " +
           "    WHEN p.price < 50000 THEN '5만원 미만' " +
           "    WHEN p.price < 100000 THEN '5-10만원' " +
           "    WHEN p.price < 200000 THEN '10-20만원' " +
           "    WHEN p.price < 500000 THEN '20-50만원' " +
           "    ELSE '50만원 이상' " +
           "END as priceRange, COUNT(p) " +
           "FROM Product p " +
           "WHERE p.business.businessId = :businessId AND p.isActive = true " +
           "GROUP BY " +
           "CASE " +
           "    WHEN p.price < 50000 THEN '5만원 미만' " +
           "    WHEN p.price < 100000 THEN '5-10만원' " +
           "    WHEN p.price < 200000 THEN '10-20만원' " +
           "    WHEN p.price < 500000 THEN '20-50만원' " +
           "    ELSE '50만원 이상' " +
           "END")
    List<Object[]> findPriceDistribution(@Param("businessId") Long businessId);

    /**
     * 상품명 자동완성을 위한 검색
     */
    @Query("SELECT DISTINCT p.name FROM Product p " +
           "WHERE p.business.businessId = :businessId " +
           "AND p.name LIKE %:keyword% " +
           "AND p.isActive = true " +
           "ORDER BY p.name")
    List<String> findProductNamesByKeyword(@Param("businessId") Long businessId, 
                                          @Param("keyword") String keyword);
}