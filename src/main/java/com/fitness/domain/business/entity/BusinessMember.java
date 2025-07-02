package com.fitness.domain.business.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 사업장 소속 회원(BUSINESS_MEMBER) 엔티티
 * 특정 사업장에 등록된 회원 정보를 관리하는 테이블
 */
@Entity
@Table(name = "business_member")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "business_member_id")
    private Long businessMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private User employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private BusinessMemberStatus status;

    @Column(name = "sms_yn", length = 1)
    private boolean smsYn = true;

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;

    @Column(name = "join_date", nullable = false)
    private LocalDate joinDate;

}