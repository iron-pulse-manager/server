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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    /**
     * 사업장 연결 신청 생성
     */
    public static BusinessEmployee createBusinessConnection(Business business, User user) {
        BusinessEmployee businessEmployee = new BusinessEmployee();
        businessEmployee.business = business;
        businessEmployee.user = user;
        businessEmployee.status = BusinessEmployeeStatus.PENDING;
        return businessEmployee;
    }

    /**
     * 완전한 사업장 연결 생성 (직접 승인용)
     */
    public static BusinessEmployee createApprovedEmployee(Business business, User user, 
                                                        String position, LocalTime workingStartTime,
                                                        LocalTime workingEndTime, LocalDate joinDate, 
                                                        String memo) {
        BusinessEmployee businessEmployee = new BusinessEmployee();
        businessEmployee.business = business;
        businessEmployee.user = user;
        businessEmployee.position = position;
        businessEmployee.workingStartTime = workingStartTime;
        businessEmployee.workingEndTime = workingEndTime;
        businessEmployee.joinDate = joinDate;
        businessEmployee.memo = memo;
        businessEmployee.status = BusinessEmployeeStatus.APPROVED;
        businessEmployee.approvedAt = LocalDateTime.now();
        return businessEmployee;
    }

    /**
     * 가입 신청 승인
     */
    public void approve() {
        if (this.status != BusinessEmployeeStatus.PENDING) {
            throw new IllegalStateException("승인대기 상태가 아닌 직원은 승인할 수 없습니다.");
        }
        
        this.status = BusinessEmployeeStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();
        if (this.joinDate == null) {
            this.joinDate = LocalDate.now();
        }
    }

    /**
     * 가입 신청 거절
     */
    public void reject() {
        if (this.status != BusinessEmployeeStatus.PENDING) {
            throw new IllegalStateException("승인대기 상태가 아닌 직원은 거절할 수 없습니다.");
        }
        this.status = BusinessEmployeeStatus.REJECTED;
    }

    /**
     * 직원 퇴사 처리
     */
    public void resign(LocalDate resignDate) {
        if (this.status != BusinessEmployeeStatus.APPROVED) {
            throw new IllegalStateException("승인된 직원만 퇴사 처리할 수 있습니다.");
        }
        this.status = BusinessEmployeeStatus.RESIGNED;
        this.resignDate = resignDate != null ? resignDate : LocalDate.now();
    }

    /**
     * 근무 정보 업데이트
     */
    public void updateWorkInfo(String position, LocalTime workingStartTime, 
                              LocalTime workingEndTime, String bankName, 
                              String accountNumber, String memo) {
        this.position = position;
        this.workingStartTime = workingStartTime;
        this.workingEndTime = workingEndTime;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.memo = memo;
    }

    /**
     * 현재 활성 상태인지 확인
     */
    public boolean isActive() {
        return this.status == BusinessEmployeeStatus.APPROVED;
    }

    /**
     * 퇴사한 직원인지 확인
     */
    public boolean isResigned() {
        return this.status == BusinessEmployeeStatus.RESIGNED;
    }

    /**
     * 승인 가능 여부 확인
     */
    public boolean canBeApproved() {
        return this.status == BusinessEmployeeStatus.PENDING;
    }
}