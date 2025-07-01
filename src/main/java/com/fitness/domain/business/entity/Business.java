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
    @JoinColumn(name = "user_id", nullable = false)
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

    /**
     * 사업장에 소속된 직원들
     */
    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BusinessEmployee> employees = new ArrayList<>();

    /**
     * 사업장에 등록된 회원들
     */
    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BusinessMember> members = new ArrayList<>();

    /**
     * Business 생성자
     */
    public static Business createBusiness(User owner, String businessName, String businessNumber) {
        Business business = new Business();
        business.owner = owner;
        business.businessName = businessName;
        business.businessNumber = businessNumber;
        business.status = BusinessStatus.PENDING;
        return business;
    }

    /**
     * 사업장 승인
     */
    public void approve() {
        this.status = BusinessStatus.ACTIVE;
    }

    /**
     * 사업장 거절
     */
    public void reject() {
        this.status = BusinessStatus.REJECTED;
    }

    /**
     * 사업장 비활성화
     */
    public void deactivate() {
        this.status = BusinessStatus.INACTIVE;
    }

    /**
     * 사업장 상태 Enum
     */
    public enum BusinessStatus {
        ACTIVE("활성"),
        INACTIVE("비활성"),
        PENDING("대기"),
        REJECTED("승인거절");

        private final String description;

        BusinessStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}