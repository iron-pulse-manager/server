package com.fitness.domain.auth.entity;

import com.fitness.common.BaseEntity;
import com.fitness.common.enums.SocialProvider;
import com.fitness.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 계정(AUTH) 엔티티
 * 사용자별 인증 정보를 관리하는 테이블 (1 User : N Auth)
 * 일반 로그인 및 소셜 로그인 정보 저장
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private SocialProvider provider;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "social_id", length = 100)
    private String socialId;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "access_token", columnDefinition = "TEXT")
    private String accessToken;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;

    /**
     * 소셜 로그인용 Auth 생성
     */
    public static Auth createSocialAuth(User user, SocialProvider provider, String socialId, 
                                       String email, String nickname, String username) {
        Auth auth = new Auth();
        auth.user = user;
        auth.provider = provider;
        auth.socialId = socialId;
        auth.email = email;
        auth.nickname = nickname;
        auth.username = username;
        return auth;
    }

    /**
     * 일반 로그인용 Auth 생성
     */
    public static Auth createGeneralAuth(User user, String username, String password, String email) {
        Auth auth = new Auth();
        auth.user = user;
        auth.provider = SocialProvider.NONE;
        auth.username = username;
        auth.password = password;
        auth.email = email;
        return auth;
    }
}