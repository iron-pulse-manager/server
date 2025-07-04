package com.fitness.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 애플 사용자 정보 DTO
 */
@Getter
@Setter
public class AppleUserInfo {

    private String sub; // 애플 사용자 ID
    private String email;
    private Boolean emailVerified;
    private Boolean isPrivateEmail;
    private Long authTime;
    private Boolean nonceSupported;
    
    // 애플 로그인 시 선택적으로 제공되는 정보
    private String firstName;
    private String lastName;
}