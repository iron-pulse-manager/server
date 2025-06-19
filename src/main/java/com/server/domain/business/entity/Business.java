package com.gymmanager.domain.business.entity;

import com.gymmanager.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 사업장 엔터티
 * 
 * 헬스장, 필라테스 등 피트니스 사업장 정보입니다.
 * Multi-tenant 아키텍처의 기준이 되는 핵심 엔터티입니다.
 */
@Entity
@Table(name = "businesses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Business extends BaseEntity {

    /**
     * 사업장명
     */
    @Column(name = "name", nullable = false, length = 200)
    @NotBlank(message = "사업장명은 필수입니다.")
    @Size(max = 200, message = "사업장명은 200자 이내여야 합니다.")
    private String name;

    /**
     * 사업장 주소
     */
    @Column(name = "address", length = 500)
    @Size(max = 500, message = "주소는 500자 이내여야 합니다.")
    private String address;

    /**
     * 사업자번호
     */
    @Column(name = "business_number", length = 20)
    @Size(max = 20, message = "사업자번호는 20자 이내여야 합니다.")
    private String businessNumber;

    /**
     * 사업장 전화번호
     */
    @Column(name = "phone", length = 20)
    @Size(max = 20, message = "전화번호는 20자 이내여야 합니다.")
    private String phone;

    /**
     * 사업장 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private BusinessStatus status = BusinessStatus.ACTIVE;

    /**
     * 사업장 상태 열거형
     */
    public enum BusinessStatus {
        ACTIVE("활성"),
        INACTIVE("비활성");

        private final String description;

        BusinessStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 비즈니스 로직: 사업장이 활성 상태인지 확인
     */
    public boolean isActive() {
        return BusinessStatus.ACTIVE.equals(status);
    }

    /**
     * 비즈니스 로직: 사업장 비활성화
     */
    public void deactivate() {
        this.status = BusinessStatus.INACTIVE;
    }

    /**
     * 비즈니스 로직: 사업장 활성화
     */
    public void activate() {
        this.status = BusinessStatus.ACTIVE;
    }

    /**
     * 비즈니스 로직: 주소가 설정되어 있는지 확인
     */
    public boolean hasAddress() {
        return address != null && !address.trim().isEmpty();
    }

    /**
     * 비즈니스 로직: 사업자번호가 설정되어 있는지 확인
     */
    public boolean hasBusinessNumber() {
        return businessNumber != null && !businessNumber.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "Business{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", businessNumber='" + businessNumber + '\'' +
                ", phone='" + phone + '\'' +
                ", status=" + status +
                '}';
    }
}