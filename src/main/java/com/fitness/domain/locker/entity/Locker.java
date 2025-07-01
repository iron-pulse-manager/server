package com.fitness.domain.locker.entity;

import com.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 락커 엔티티
 * 사업장의 락커 정보를 관리
 */
@Entity
@Table(name = "lockers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Locker extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "locker_id")
    private Long lockerId;

    @Column(name = "business_id", nullable = false)
    private Long businessId; // 사업장 ID

    @Column(name = "locker_number", nullable = false)
    private String lockerNumber; // 락커 번호

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private LockerStatus status = LockerStatus.AVAILABLE; // 상태

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo; // 메모

    // 연관관계 매핑
    @OneToMany(mappedBy = "locker", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LockerAssign> assignments = new ArrayList<>();

    /**
     * 락커 상태
     */
    public enum LockerStatus {
        AVAILABLE("이용가능"),
        IN_USE("이용중"),
        EXPIRED("만료"),
        MAINTENANCE("점검중"),
        OUT_OF_ORDER("고장");

        private final String description;

        LockerStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // 편의 메서드
    public void addAssignment(LockerAssign assignment) {
        assignments.add(assignment);
        assignment.setLocker(this);
    }

    public void removeAssignment(LockerAssign assignment) {
        assignments.remove(assignment);
        assignment.setLocker(null);
    }

    // 비즈니스 메서드

    /**
     * 락커 사용 시작
     */
    public void startUse() {
        if (LockerStatus.AVAILABLE.equals(status)) {
            this.status = LockerStatus.IN_USE;
        }
    }

    /**
     * 락커 사용 종료
     */
    public void endUse() {
        if (LockerStatus.IN_USE.equals(status) || LockerStatus.EXPIRED.equals(status)) {
            this.status = LockerStatus.AVAILABLE;
        }
    }

    /**
     * 락커 만료 처리
     */
    public void expire() {
        if (LockerStatus.IN_USE.equals(status)) {
            this.status = LockerStatus.EXPIRED;
        }
    }

    /**
     * 락커 점검 상태로 변경
     */
    public void setMaintenance() {
        this.status = LockerStatus.MAINTENANCE;
    }

    /**
     * 락커 고장 상태로 변경
     */
    public void setOutOfOrder() {
        this.status = LockerStatus.OUT_OF_ORDER;
    }

    /**
     * 사용 가능한 락커인지 확인
     */
    public boolean isAvailable() {
        return LockerStatus.AVAILABLE.equals(status);
    }

    /**
     * 사용 중인 락커인지 확인
     */
    public boolean isInUse() {
        return LockerStatus.IN_USE.equals(status);
    }

    /**
     * 만료된 락커인지 확인
     */
    public boolean isExpired() {
        return LockerStatus.EXPIRED.equals(status);
    }

    /**
     * 현재 사용자가 있는지 확인
     */
    public boolean hasCurrentUser() {
        return assignments.stream()
                .anyMatch(assignment -> assignment.isActive());
    }

    /**
     * 현재 배정된 사용자 조회
     */
    public LockerAssign getCurrentAssignment() {
        return assignments.stream()
                .filter(LockerAssign::isActive)
                .findFirst()
                .orElse(null);
    }

    /**
     * 락커 정보 업데이트
     */
    public void updateLockerInfo(String lockerNumber, String memo) {
        this.lockerNumber = lockerNumber;
        this.memo = memo;
    }

    /**
     * 락커 상태 요약
     */
    public String getLockerSummary() {
        return String.format("락커 %s - %s", 
                lockerNumber, 
                status.getDescription());
    }
}