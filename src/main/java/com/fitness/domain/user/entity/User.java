package com.fitness.domain.user.entity;

import com.fitness.common.BaseEntity;
import com.fitness.common.enums.UserStatus;
import com.fitness.common.enums.UserType;
import com.fitness.domain.auth.entity.Auth;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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

    @Column(name = "user_type", length = 100, nullable = false, unique = true)
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
     * User와 Auth는 1:1 관계
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_id")
    private Auth auth;
}