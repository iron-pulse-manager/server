package com.fitness.domain.membership.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.business.entity.Business;
import com.fitness.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 회원권 정지 이력(MEMBERSHIP_SUSPENSION) 엔티티
 * 회원권 정지 내역을 관리하는 테이블
 */
@Entity
@Table(name = "membership_suspension")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MembershipSuspension extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membership_suspension_id")
    private Long membershipSuspensionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approve_employee_id")
    private User approveEmployee;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

}