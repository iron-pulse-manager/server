package com.gymmanager.domain.product.entity;

import com.gymmanager.common.entity.TenantBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * 상품 엔터티
 * 
 * 헬스장에서 판매하는 상품 정보입니다.
 * 회원권, 개인레슨, 락커, 기타 상품 등을 포함합니다.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends TenantBaseEntity {

    /**
     * 상품명
     */
    @Column(name = "name", nullable = false, length = 200)
    @NotBlank(message = "상품명은 필수입니다.")
    @Size(max = 200, message = "상품명은 200자 이내여야 합니다.")
    private String name;

    /**
     * 상품 유형
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @NotNull(message = "상품 유형은 필수입니다.")
    private ProductType type;

    /**
     * 상품 가격
     */
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "가격은 필수입니다.")
    @DecimalMin(value = "0.01", message = "가격은 0.01 이상이어야 합니다.")
    @Digits(integer = 8, fraction = 2, message = "가격은 최대 8자리 정수와 2자리 소수를 가질 수 있습니다.")
    private BigDecimal price;

    /**
     * 기간(일) - 개인레슨은 NULL
     */
    @Column(name = "duration_days")
    @Min(value = 1, message = "기간은 1일 이상이어야 합니다.")
    private Integer durationDays;

    /**
     * 횟수 - 개인레슨용
     */
    @Column(name = "session_count")
    @Min(value = 1, message = "횟수는 1회 이상이어야 합니다.")
    private Integer sessionCount;

    /**
     * 상품 설명
     */
    @Column(name = "description", columnDefinition = "TEXT")
    @Size(max = 1000, message = "상품 설명은 1000자 이내여야 합니다.")
    private String description;

    /**
     * 활성화 여부
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * 상품 유형 열거형
     */
    public enum ProductType {
        MEMBERSHIP("회원권"),
        PERSONAL_TRAINING("개인레슨"),
        LOCKER("락커"),
        OTHER("기타");

        private final String description;

        ProductType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 비즈니스 로직: 상품이 활성 상태인지 확인
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }

    /**
     * 비즈니스 로직: 상품 비활성화
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * 비즈니스 로직: 상품 활성화
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * 비즈니스 로직: 회원권 상품인지 확인
     */
    public boolean isMembership() {
        return ProductType.MEMBERSHIP.equals(type);
    }

    /**
     * 비즈니스 로직: 개인레슨 상품인지 확인
     */
    public boolean isPersonalTraining() {
        return ProductType.PERSONAL_TRAINING.equals(type);
    }

    /**
     * 비즈니스 로직: 락커 상품인지 확인
     */
    public boolean isLocker() {
        return ProductType.LOCKER.equals(type);
    }

    /**
     * 비즈니스 로직: 기간 기반 상품인지 확인 (회원권, 락커)
     */
    public boolean isDurationBased() {
        return isMembership() || isLocker();
    }

    /**
     * 비즈니스 로직: 횟수 기반 상품인지 확인 (개인레슨)
     */
    public boolean isSessionBased() {
        return isPersonalTraining();
    }

    /**
     * 비즈니스 로직: 상품 정보 유효성 검증
     */
    @PrePersist
    @PreUpdate
    private void validateProduct() {
        if (isDurationBased() && durationDays == null) {
            throw new IllegalStateException(
                type.getDescription() + " 상품은 기간이 필수입니다.");
        }
        
        if (isSessionBased() && sessionCount == null) {
            throw new IllegalStateException(
                type.getDescription() + " 상품은 횟수가 필수입니다.");
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + getId() +
                ", businessId=" + getBusinessId() +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", price=" + price +
                ", durationDays=" + durationDays +
                ", sessionCount=" + sessionCount +
                ", isActive=" + isActive +
                '}';
    }
}