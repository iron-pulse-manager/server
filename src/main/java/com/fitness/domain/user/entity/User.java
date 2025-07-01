package com.fitness.domain.user.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.auth.entity.Auth;
import com.fitness.domain.business.entity.BusinessEmployee;
import com.fitness.domain.business.entity.BusinessMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 유저(USER) 엔티티
 * 사용자 기본 정보를 관리하는 테이블
 */
@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private UserStatus status;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "ci")
    private String ci;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Column(name = "address", length = 500)
    private String address;

    /**
     * User와 Auth는 1:1 관계
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_id")
    private Auth auth;

    /**
     * User가 직원으로 속한 사업장들 (1:N)
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BusinessEmployee> businessEmployees = new ArrayList<>();

    /**
     * User가 회원으로 속한 사업장들 (1:N)
     */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BusinessMember> businessMembers = new ArrayList<>();

    /**
     * User 생성자
     */
    public static User createUser(String name, Auth auth) {
        User user = new User();
        user.name = name;
        user.auth = auth;
        user.status = UserStatus.ACTIVE;
        return user;
    }

    /**
     * 사용자 정보 업데이트
     */
    public void updatePersonalInfo(String name, String phoneNumber, LocalDate birthday, 
                                 Gender gender, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
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
    public enum UserStatus {
        ACTIVE("활성"),
        INACTIVE("비활성");

        private final String description;

        UserStatus(String description) {
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