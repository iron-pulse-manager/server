package com.fitness.domain.member.entity;

import com.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 회원 엔티티
 * 개별 회원 정보를 관리하는 테이블
 */
@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private MemberStatus status;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Column(name = "memo", length = 1000)
    private String memo;

    @Column(name = "marketing_agree")
    private Boolean marketingAgree;

    /**
     * Member 생성자
     */
    public static Member createMember(String name, String phone) {
        Member member = new Member();
        member.name = name;
        member.phone = phone;
        member.status = MemberStatus.ACTIVE;
        member.marketingAgree = false;
        return member;
    }

    /**
     * 회원 정보 업데이트
     */
    public void updatePersonalInfo(String name, String phone, String email, 
                                 LocalDate birthday, Gender gender, String address, String memo) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
        this.memo = memo;
    }

    /**
     * 나이 계산 메서드
     */
    public Integer getAge() {
        if (birthday == null) {
            return null;
        }
        return LocalDate.now().getYear() - birthday.getYear();
    }

    /**
     * 회원 상태 Enum
     */
    public enum MemberStatus {
        ACTIVE("활성"),
        INACTIVE("비활성"),
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
     * 성별 Enum
     */
    public enum Gender {
        MALE("남성"),
        FEMALE("여성");

        private final String description;

        Gender(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}