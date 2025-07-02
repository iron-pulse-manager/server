package com.fitness.domain.business.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 사업장(BUSINESS) 엔티티
 * 체육시설업 사업장 정보를 관리하는 테이블
 */
@Entity
@Table(name = "business")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Business extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "business_id")
    private Long businessId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(name = "business_number", length = 20, unique = true, nullable = false)
    private String businessNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private BusinessStatus status;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "address", length = 500)
    private String address;

}