package com.fitness.domain.product.repository;

import com.fitness.domain.product.entity.Product;
import com.fitness.domain.product.entity.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 상품 커스텀 리포지토리 인터페이스
 * Querydsl을 사용한 복잡한 조회 메서드 정의
 */
public interface ProductRepositoryCustom {

    /**
     * 복잡한 조건으로 상품 검색
     */
    Page<Product> searchProductsWithConditions(
            Long businessId,
            String productName,
            ProductType productType,
            Long minPrice,
            Long maxPrice,
            Integer minValidDays,
            Integer maxValidDays,
            Integer minUsageCount,
            Integer maxUsageCount,
            Boolean isActive,
            String description,
            Pageable pageable
    );

    /**
     * 특정 사업장의 상품 통계 조회
     */
    ProductStats getProductStatsByBusinessId(Long businessId);

    /**
     * 상품 유형별 매출 통계
     */
    List<ProductTypeStats> getProductTypeStatsByBusinessId(Long businessId);

    /**
     * 인기 상품 분석 (매출액 기준)
     */
    List<Product> getTopSellingProducts(Long businessId, int limit);

    /**
     * 상품 가격 분포 분석
     */
    List<PriceRangeStats> getPriceRangeDistribution(Long businessId);

    /**
     * 기간별 상품 판매 분석
     */
    List<ProductSalesStats> getProductSalesStatsByPeriod(Long businessId, String period);

    /**
     * 상품 재고 부족 알림 (usage_count 기반)
     */
    List<Product> findLowStockProducts(Long businessId, Integer threshold);

    /**
     * 상품 성과 분석 (매출 대비 판매량)
     */
    List<ProductPerformanceStats> getProductPerformanceStats(Long businessId);

    /**
     * 미사용 상품 조회 (한 번도 판매되지 않은 상품)
     */
    List<Product> findUnusedProducts(Long businessId);

    /**
     * 상품 트렌드 분석 (월별 판매량 변화)
     */
    List<ProductTrendStats> getProductTrendStats(Long businessId, int months);

    // 통계용 DTO 인터페이스들
    interface ProductStats {
        Long getTotalProducts();
        Long getActiveProducts();
        Long getInactiveProducts();
        Long getMembershipProducts();
        Long getPersonalTrainingProducts();
        Long getLockerProducts();
        Long getOtherProducts();
        Long getPeriodBasedProducts();
        Long getCountBasedProducts();
        Long getAveragePrice();
        Long getMinPrice();
        Long getMaxPrice();
    }

    interface ProductTypeStats {
        ProductType getProductType();
        Long getProductCount();
        Long getTotalSales();
        Long getAveragePrice();
        Long getTotalRevenue();
        Double getRevenuePercentage();
    }

    interface PriceRangeStats {
        String getPriceRange();
        Long getProductCount();
        Double getPercentage();
        Long getTotalRevenue();
    }

    interface ProductSalesStats {
        Long getProductId();
        String getProductName();
        ProductType getProductType();
        Long getSalesCount();
        Long getTotalRevenue();
        Long getAveragePrice();
        String getPeriod();
    }

    interface ProductPerformanceStats {
        Long getProductId();
        String getProductName();
        ProductType getProductType();
        Long getPrice();
        Long getSalesCount();
        Long getTotalRevenue();
        Double getProfitMargin();
        String getPerformanceRating();
    }

    interface ProductTrendStats {
        Long getProductId();
        String getProductName();
        String getYearMonth();
        Long getSalesCount();
        Long getRevenue();
        Double getGrowthRate();
    }
}