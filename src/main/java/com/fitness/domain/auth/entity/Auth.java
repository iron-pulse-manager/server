package com.fitness.domain.auth.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 계정(AUTH) 엔티티
 * 사용자 인증 정보를 관리하는 테이블
 */
@Entity
@Table(name = "auth")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Auth extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id")
    private Long authId;

    @Column(name = "username", length = 50, unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "kakao_id")
    private String kakaoId;

    @Column(name = "apple_id")
    private String appleId;

}