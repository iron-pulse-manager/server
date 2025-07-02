package com.fitness.domain.payment.entity;

import com.fitness.common.BaseEntity;
import com.fitness.common.enums.PaymentMethod;
import com.fitness.domain.business.entity.Business;
import com.fitness.domain.product.entity.Product;
import com.fitness.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 결제(PAYMENT) 엔티티
 * 회원의 상품 결제 정보를 관리하는 테이블
 */
@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private PaymentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultant_id")
    private User consultant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private User trainer;

    @Column(name = "product_price")
    private Integer productPrice;

    @Column(name = "actual_price")
    private Integer actualPrice;

    @Column(name = "consultant_commission_price")
    private Integer consultantCommissionPrice;

    @Column(name = "outstanding_amount")
    private Integer outstandingAmount;

    @Column(name = "initial_outstanding_amount")
    private Integer initialOutstandingAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "purchase_purpose")
    private String purchasePurpose;

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;

}