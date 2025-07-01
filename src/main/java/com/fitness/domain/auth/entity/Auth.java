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

    /**
     * Auth와 User는 1:1 관계
     */
    @OneToOne(mappedBy = "auth", fetch = FetchType.LAZY)
    private User user;

    /**
     * 일반 로그인용 Auth 생성자
     */
    public static Auth createNormalAuth(String username, String password) {
        Auth auth = new Auth();
        auth.username = username;
        auth.password = password;
        return auth;
    }

    /**
     * 카카오 로그인용 Auth 생성자
     */
    public static Auth createKakaoAuth(String kakaoId) {
        Auth auth = new Auth();
        auth.kakaoId = kakaoId;
        return auth;
    }

    /**
     * 애플 로그인용 Auth 생성자
     */
    public static Auth createAppleAuth(String appleId) {
        Auth auth = new Auth();
        auth.appleId = appleId;
        return auth;
    }

    /**
     * 로그인 유형 판별 메서드
     */
    public LoginType getLoginType() {
        if (kakaoId != null) {
            return LoginType.KAKAO;
        } else if (appleId != null) {
            return LoginType.APPLE;
        } else {
            return LoginType.NORMAL;
        }
    }

    /**
     * 로그인 유형 Enum
     */
    public enum LoginType {
        NORMAL, KAKAO, APPLE
    }
}