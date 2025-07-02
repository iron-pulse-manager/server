package com.fitness.domain.attendance.entity;

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
 * 출석(ATTENDANCE) 엔티티
 * 회원의 사업장 출석 정보를 관리하는 테이블
 */
@Entity
@Table(name = "attendance")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long attendanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @Column(name = "attendance_date", nullable = false)
    private LocalDateTime attendanceDate;

}