package com.fitness.domain.product.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.business.entity.Business;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    @Column(name = "product_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;


    @Column(name = "valid_day")
    private Integer validDay;

    @Column(name = "usage_cnt")
    private Integer usageCnt;


    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

}