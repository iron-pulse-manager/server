package com.fitness.domain.product.entity;

import com.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 상품 엔티티
 */
@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "business_id", nullable = false)
    private Long businessId;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @Column(name = "product_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Column(name = "price", nullable = false, precision = 10, scale = 0)
    private Long price;

    @Column(name = "valid_days")
    private Integer validDays;

    @Column(name = "usage_count")
    private Integer usageCount;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    // 팩토리 메서드
    /**
     * 회원권 상품 생성 (기간제)
     */
    public static Product createMembershipProduct(Long businessId, String productName, 
                                                Long price, Integer validDays, String description) {
        return Product.builder()
                .businessId(businessId)
                .productName(productName)
                .productType(ProductType.MEMBERSHIP)
                .price(price)
                .validDays(validDays)
                .description(description)
                .isActive(true)
                .build();
    }

    /**
     * 개인레슨 상품 생성 (횟수제)
     */
    public static Product createPersonalTrainingProduct(Long businessId, String productName, 
                                                      Long price, Integer usageCount, String description) {
        return Product.builder()
                .businessId(businessId)
                .productName(productName)
                .productType(ProductType.PERSONAL_TRAINING)
                .price(price)
                .usageCount(usageCount)
                .description(description)
                .isActive(true)
                .build();
    }

    /**
     * 락커 상품 생성 (기간제)
     */
    public static Product createLockerProduct(Long businessId, String productName, 
                                            Long price, Integer validDays, String description) {
        return Product.builder()
                .businessId(businessId)
                .productName(productName)
                .productType(ProductType.LOCKER)
                .price(price)
                .validDays(validDays)
                .description(description)
                .isActive(true)
                .build();
    }

    /**
     * 기타 상품 생성 (횟수제 또는 기간제)
     */
    public static Product createOtherProduct(Long businessId, String productName, Long price, 
                                           Integer validDays, Integer usageCount, String description) {
        return Product.builder()
                .businessId(businessId)
                .productName(productName)
                .productType(ProductType.OTHERS)
                .price(price)
                .validDays(validDays)
                .usageCount(usageCount)
                .description(description)
                .isActive(true)
                .build();
    }

    // 비즈니스 메서드
    /**
     * 상품 정보 업데이트
     */
    public void updateProduct(String productName, Long price, Integer validDays, 
                            Integer usageCount, String description) {
        this.productName = productName;
        this.price = price;
        this.validDays = validDays;
        this.usageCount = usageCount;
        this.description = description;
    }

    /**
     * 상품 활성화/비활성화
     */
    public void changeActiveStatus(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * 상품 활성화
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * 상품 비활성화
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * 기간제 상품인지 확인
     */
    public boolean isPeriodBased() {
        return validDays != null && validDays > 0;
    }

    /**
     * 횟수제 상품인지 확인
     */
    public boolean isCountBased() {
        return usageCount != null && usageCount > 0;
    }

    /**
     * 회원권 상품인지 확인
     */
    public boolean isMembershipProduct() {
        return productType == ProductType.MEMBERSHIP;
    }

    /**
     * 개인레슨 상품인지 확인
     */
    public boolean isPersonalTrainingProduct() {
        return productType == ProductType.PERSONAL_TRAINING;
    }

    /**
     * 락커 상품인지 확인
     */
    public boolean isLockerProduct() {
        return productType == ProductType.LOCKER;
    }

    /**
     * 기타 상품인지 확인
     */
    public boolean isOtherProduct() {
        return productType == ProductType.OTHERS;
    }

    /**
     * 상품 유형 열거형
     */
    public enum ProductType {
        /**
         * 회원권 (헬스, 필라테스 등)
         */
        MEMBERSHIP("회원권"),
        
        /**
         * 개인레슨 (PT, 개인 필라테스 등)
         */
        PERSONAL_TRAINING("개인레슨"),
        
        /**
         * 락커 이용권
         */
        LOCKER("락커"),
        
        /**
         * 기타 상품 (프로틴, 운동용품 등)
         */
        OTHERS("기타");

        private final String description;

        ProductType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}