package com.fitness.domain.membership.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.locker.entity.Locker;
import com.fitness.domain.payment.entity.Payment;
import com.fitness.domain.product.entity.Product;
import com.fitness.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 이용권(MEMBERSHIP) 엔티티
 * 회원의 이용권 정보를 관리하는 테이블
 */
@Entity
@Table(name = "membership")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Membership extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membership_id")
    private Long membershipId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20, nullable = false)
    private MembershipType type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locker_id")
    private Locker locker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "service_start_date", nullable = false)
    private LocalDate serviceStartDate;

    @Column(name = "service_end_date", nullable = false)
    private LocalDate serviceEndDate;

    /**
     * Membership 생성자
     */
    public static Membership createMembership(Payment payment, User user, MembershipType type,
                                            Product product, LocalDate serviceStartDate,
                                            LocalDate serviceEndDate, Locker locker) {
        Membership membership = new Membership();
        membership.payment = payment;
        membership.user = user;
        membership.type = type;
        membership.product = product;
        membership.serviceStartDate = serviceStartDate;
        membership.serviceEndDate = serviceEndDate;
        membership.locker = locker;
        return membership;
    }

    /**
     * 이용권 연장
     */
    public void extend(int days) {
        this.serviceEndDate = this.serviceEndDate.plusDays(days);
    }

    /**
     * 락커 배정
     */
    public void assignLocker(Locker locker) {
        this.locker = locker;
    }

    /**
     * 락커 해제
     */
    public void removeLocker() {
        this.locker = null;
    }

    /**
     * 이용권 만료 여부 확인
     */
    public boolean isExpired() {
        return LocalDate.now().isAfter(this.serviceEndDate);
    }

    /**
     * 이용권 만료 임박 여부 확인 (30일 이내)
     */
    public boolean isExpiringSoon() {
        return LocalDate.now().plusDays(30).isAfter(this.serviceEndDate) && !isExpired();
    }

    /**
     * 이용권 유형 Enum
     */
    public enum MembershipType {
        GYM("회원권"),
        PT("PT"),
        PILATES("필라테스");

        private final String description;

        MembershipType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}