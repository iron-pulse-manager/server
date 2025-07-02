package com.fitness.domain.business.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 사업장 소속 직원(BUSINESS_EMPLOYEE) 엔티티
 * 특정 사업장에 소속된 직원 정보를 관리하는 테이블
 */
@Entity
@Table(name = "business_employee")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BusinessEmployee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "business_employee_id")
    private Long businessEmployeeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private BusinessEmployeeStatus status;

    @Column(name = "position", length = 50)
    private String position;

    @Column(name = "working_start_time")
    private LocalTime workingStartTime;

    @Column(name = "working_end_time")
    private LocalTime workingEndTime;

    @Column(name = "bank_name", length = 50)
    private String bankName;

    @Column(name = "account_number", length = 50)
    private String accountNumber;

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Column(name = "resign_date")
    private LocalDate resignDate;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

}