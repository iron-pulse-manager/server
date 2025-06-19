package com.gymmanager.domain.member.entity;

import com.gymmanager.common.entity.TenantBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;

/**
 * 회원 엔터티
 * 
 * 헬스장 회원의 기본 정보를 관리합니다.
 * 사업장별로 독립적으로 관리되는 Multi-tenant 엔터티입니다.
 */
@Entity
@Table(
    name = "members",
    uniqueConstraints = {
        @UniqueConstraint(name = "unique_business_member_number", 
                         columnNames = {"business_id", "member_number"}),
        @UniqueConstraint(name = "unique_phone", columnNames = {"phone"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends TenantBaseEntity {

    /**
     * 회원번호 (사업장 내 고유)
     */
    @Column(name = "member_number", length = 50)
    @Size(max = 50, message = "회원번호는 50자 이내여야 합니다.")
    private String memberNumber;

    /**
     * 회원 이름
     */
    @Column(name = "name", nullable = false, length = 100)
    @NotBlank(message = "이름은 필수입니다.")
    @Size(max = 100, message = "이름은 100자 이내여야 합니다.")
    private String name;

    /**
     * 핸드폰 번호 (전체 시스템에서 고유)
     */
    @Column(name = "phone", unique = true, length = 20)
    @Size(max = 20, message = "핸드폰 번호는 20자 이내여야 합니다.")
    @Pattern(regexp = "^01[0-9]-[0-9]{3,4}-[0-9]{4}$", 
             message = "올바른 핸드폰 번호 형식이 아닙니다. (예: 010-1234-5678)")
    private String phone;

    /**
     * 이메일 주소
     */
    @Column(name = "email", length = 255)
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Size(max = 255, message = "이메일은 255자 이내여야 합니다.")
    private String email;

    /**
     * 생년월일
     */
    @Column(name = "birth_date")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birthDate;

    /**
     * 성별
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    /**
     * 주소
     */
    @Column(name = "address", length = 500)
    @Size(max = 500, message = "주소는 500자 이내여야 합니다.")
    private String address;

    /**
     * 프로필 이미지 URL
     */
    @Column(name = "profile_image_url", length = 500)
    @Size(max = 500, message = "프로필 이미지 URL은 500자 이내여야 합니다.")
    private String profileImageUrl;

    /**
     * 특이사항 메모
     */
    @Column(name = "memo", columnDefinition = "TEXT")
    @Size(max = 1000, message = "메모는 1000자 이내여야 합니다.")
    private String memo;

    /**
     * 문자 수신 동의
     */
    @Column(name = "sms_consent", nullable = false)
    @Builder.Default
    private Boolean smsConsent = false;

    /**
     * 앱 이용 여부
     */
    @Column(name = "app_usage", nullable = false)
    @Builder.Default
    private Boolean appUsage = false;

    /**
     * 회원 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private MemberStatus status = MemberStatus.ACTIVE;

    /**
     * 성별 열거형
     */
    public enum Gender {
        MALE("남성"),
        FEMALE("여성"),
        OTHER("기타");

        private final String description;

        Gender(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 회원 상태 열거형
     */
    public enum MemberStatus {
        ACTIVE("활성"),
        EXPIRED("만료"),
        SUSPENDED("정지");

        private final String description;

        MemberStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 비즈니스 로직: 회원이 활성 상태인지 확인
     */
    public boolean isActive() {
        return MemberStatus.ACTIVE.equals(status);
    }

    /**
     * 비즈니스 로직: 회원이 만료 상태인지 확인
     */
    public boolean isExpired() {
        return MemberStatus.EXPIRED.equals(status);
    }

    /**
     * 비즈니스 로직: 회원이 정지 상태인지 확인
     */
    public boolean isSuspended() {
        return MemberStatus.SUSPENDED.equals(status);
    }

    /**
     * 비즈니스 로직: 나이 계산
     */
    public Integer getAge() {
        if (birthDate == null) {
            return null;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    /**
     * 비즈니스 로직: 문자 수신 가능한지 확인
     */
    public boolean canReceiveSms() {
        return Boolean.TRUE.equals(smsConsent) && phone != null && !phone.trim().isEmpty();
    }

    /**
     * 비즈니스 로직: 앱을 사용하는지 확인
     */
    public boolean isAppUser() {
        return Boolean.TRUE.equals(appUsage);
    }

    /**
     * 비즈니스 로직: 이메일이 설정되어 있는지 확인
     */
    public boolean hasEmail() {
        return email != null && !email.trim().isEmpty();
    }

    /**
     * 비즈니스 로직: 프로필 이미지가 설정되어 있는지 확인
     */
    public boolean hasProfileImage() {
        return profileImageUrl != null && !profileImageUrl.trim().isEmpty();
    }

    /**
     * 비즈니스 로직: 특이사항 메모가 있는지 확인
     */
    public boolean hasMemo() {
        return memo != null && !memo.trim().isEmpty();
    }

    /**
     * 비즈니스 로직: 주소가 설정되어 있는지 확인
     */
    public boolean hasAddress() {
        return address != null && !address.trim().isEmpty();
    }

    /**
     * 회원 상태 변경 메서드들
     */
    public void activate() {
        this.status = MemberStatus.ACTIVE;
    }

    public void expire() {
        this.status = MemberStatus.EXPIRED;
    }

    public void suspend() {
        this.status = MemberStatus.SUSPENDED;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + getId() +
                ", businessId=" + getBusinessId() +
                ", memberNumber='" + memberNumber + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                '}';
    }
}