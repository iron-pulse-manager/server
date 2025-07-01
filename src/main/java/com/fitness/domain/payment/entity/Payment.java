package com.fitness.domain.payment.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.business.entity.Business;
import com.fitness.domain.product.entity.Product;
import com.fitness.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultant_id")
    private User consultant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private User trainer;

    @Column(name = "product_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal productPrice;

    @Column(name = "actual_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal actualPrice;

    @Column(name = "consultant_employee_commission", precision = 10, scale = 2)
    private BigDecimal consultantEmployeeCommission = BigDecimal.ZERO;

    @Column(name = "outstanding_amount", precision = 10, scale = 2)
    private BigDecimal outstandingAmount = BigDecimal.ZERO;

    @Column(name = "initial_outstanding_amount", precision = 10, scale = 2)
    private BigDecimal initialOutstandingAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 20, nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "purchase_purpose")
    private String purchasePurpose;

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private PaymentStatus status;

    /**
     * Payment 생성자
     */
    public static Payment createPayment(Business business, User member, Product product,
                                      User consultant, User trainer, BigDecimal productPrice,
                                      BigDecimal actualPrice, PaymentMethod paymentMethod,
                                      String purchasePurpose, String memo) {
        Payment payment = new Payment();
        payment.business = business;
        payment.member = member;
        payment.product = product;
        payment.consultant = consultant;
        payment.trainer = trainer;
        payment.productPrice = productPrice;
        payment.actualPrice = actualPrice;
        payment.paymentMethod = paymentMethod;
        payment.paymentDate = LocalDateTime.now();
        payment.purchasePurpose = purchasePurpose;
        payment.memo = memo;
        payment.status = PaymentStatus.COMPLETED;
        
        // 미수금 계산 (상품 가격 - 실제 결제 금액)
        payment.outstandingAmount = productPrice.subtract(actualPrice);
        payment.initialOutstandingAmount = payment.outstandingAmount;
        
        return payment;
    }

    /**
     * 미수금 결제 처리
     */
    public void payOutstandingAmount(BigDecimal paidAmount) {
        if (this.outstandingAmount.compareTo(paidAmount) < 0) {
            throw new IllegalArgumentException("결제 금액이 미수금보다 클 수 없습니다.");
        }
        this.outstandingAmount = this.outstandingAmount.subtract(paidAmount);
    }

    /**
     * 결제 취소
     */
    public void cancel() {
        this.status = PaymentStatus.CANCELLED;
    }

    /**
     * 미수금 여부 확인
     */
    public boolean hasOutstandingAmount() {
        return this.outstandingAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 결제 방법 Enum
     */
    public enum PaymentMethod {
        CARD("카드"),
        CASH("현금"),
        BANK_TRANSFER("계좌이체");

        private final String description;

        PaymentMethod(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 결제 상태 Enum
     */
    public enum PaymentStatus {
        PENDING("대기"),
        COMPLETED("완료"),
        CANCELLED("취소");

        private final String description;

        PaymentStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}