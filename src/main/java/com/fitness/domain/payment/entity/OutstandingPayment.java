package com.fitness.domain.payment.entity;

import com.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 미수금 결제 이력 엔티티
 * 원본 결제 건의 미수금을 나중에 결제한 내역을 관리
 */
@Entity
@Table(name = "outstanding_payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OutstandingPayment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "outstanding_payment_id")
    private Long outstandingPaymentId;

    @Column(name = "original_payment_id", nullable = false)
    private Long originalPaymentId;

    @Column(name = "business_id", nullable = false)
    private Long businessId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "paid_amount", nullable = false, precision = 10, scale = 0)
    private Long paidAmount;

    @Column(name = "payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "processed_by")
    private Long processedBy;

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;

    // 연관관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_payment_id", insertable = false, updatable = false)
    private Payment originalPayment;

    // 팩토리 메서드
    /**
     * 미수금 결제 생성
     */
    public static OutstandingPayment createOutstandingPayment(
            Long originalPaymentId, Long businessId, Long memberId,
            Long paidAmount, PaymentMethod paymentMethod, Long processedBy, String memo) {
        return OutstandingPayment.builder()
                .originalPaymentId(originalPaymentId)
                .businessId(businessId)
                .memberId(memberId)
                .paidAmount(paidAmount)
                .paymentMethod(paymentMethod)
                .paymentDate(LocalDateTime.now())
                .processedBy(processedBy)
                .memo(memo)
                .build();
    }

    // 비즈니스 메서드
    /**
     * 결제 정보 업데이트
     */
    public void updatePaymentInfo(PaymentMethod paymentMethod, String memo) {
        this.paymentMethod = paymentMethod;
        this.memo = memo;
    }

    /**
     * 처리자 정보 업데이트
     */
    public void updateProcessedBy(Long processedBy) {
        this.processedBy = processedBy;
    }

    /**
     * 현금 결제인지 확인
     */
    public boolean isCashPayment() {
        return this.paymentMethod == PaymentMethod.CASH;
    }

    /**
     * 카드 결제인지 확인
     */
    public boolean isCardPayment() {
        return this.paymentMethod == PaymentMethod.CARD;
    }

    /**
     * 계좌이체 결제인지 확인
     */
    public boolean isTransferPayment() {
        return this.paymentMethod == PaymentMethod.TRANSFER;
    }

    /**
     * 결제 방법 열거형
     */
    public enum PaymentMethod {
        /**
         * 카드 결제
         */
        CARD("카드"),
        
        /**
         * 현금 결제
         */
        CASH("현금"),
        
        /**
         * 계좌이체
         */
        TRANSFER("계좌이체");

        private final String description;

        PaymentMethod(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}