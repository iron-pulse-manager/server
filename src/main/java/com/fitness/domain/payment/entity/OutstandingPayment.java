package com.fitness.domain.payment.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.business.entity.Business;
import com.fitness.common.enums.PaymentMethod;
import com.fitness.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 미수금 결제 이력 엔티티
 * 원본 결제 건의 미수금을 나중에 결제한 내역을 관리
 */
@Entity
@Table(name = "outstanding_payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OutstandingPayment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "outstanding_payment_id")
    private Long outstandingPaymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment originalPayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private User member;

    @Column(name = "paid_amount", nullable = false)
    private Integer paidAmount;

    @Column(name = "payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;
}