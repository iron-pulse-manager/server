package com.fitness.domain.user.entity;

import com.fitness.common.BaseEntity;
import com.fitness.common.enums.Gender;
import com.fitness.common.enums.UserStatus;
import com.fitness.common.enums.UserType;
import com.fitness.domain.auth.entity.Auth;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", length = 100, nullable = false)
    private UserType userType;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "ci")
    private String ci;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Column(name = "address", length = 500)
    private String address;

    /**
     * User와 Auth는 1:N 관계 (1사용자 : N인증방식)
     * 사용자 1명이 일반로그인 + 여러 소셜로그인을 가질 수 있음
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Auth> authList = new ArrayList<>();

    /**
     * 소셜 로그인용 User 생성
     */
    public static User createSocialUser(UserType userType, String name, String photoUrl) {
        User user = new User();
        user.userType = userType;
        user.name = name;
        user.photoUrl = photoUrl;
        user.status = UserStatus.ACTIVE;
        return user;
    }

    /**
     * 일반 로그인용 User 생성 (OWNER용)
     */
    public static User createGeneralUser(String name, String phoneNumber, String email) {
        User user = new User();
        user.userType = UserType.OWNER;
        user.name = name;
        user.phoneNumber = phoneNumber;
        user.status = UserStatus.ACTIVE;
        return user;
    }
}