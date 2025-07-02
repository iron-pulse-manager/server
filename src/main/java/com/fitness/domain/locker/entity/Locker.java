package com.fitness.domain.locker.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.business.entity.Business;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    @Column(name = "locker_number", nullable = false)
    private Integer lockerNumber; // 락커 번호

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private LockerStatus status = LockerStatus.AVAILABLE; // 상태

}