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
import java.time.LocalDateTime;

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
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

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
    private LocalDateTime serviceStartDate;

    @Column(name = "service_end_date", nullable = false)
    private LocalDateTime serviceEndDate;

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo; // 메모

}