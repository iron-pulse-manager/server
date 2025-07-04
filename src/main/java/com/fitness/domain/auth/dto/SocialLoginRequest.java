package com.fitness.domain.auth.dto;

import com.fitness.common.enums.SocialProvider;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 소셜 로그인 요청 DTO
 */
@Getter
@Setter
@Schema(description = "소셜 로그인 요청")
public class SocialLoginRequest {

    @NotNull(message = "소셜 제공자는 필수입니다.")
    @Schema(description = "소셜 제공자", example = "KAKAO")
    private SocialProvider provider;

    @NotBlank(message = "액세스 토큰은 필수입니다.")
    @Schema(description = "소셜 액세스 토큰", example = "kakao_access_token_here")
    private String accessToken;

    @Schema(description = "소셜 ID (카카오/애플에서 제공)", example = "1234567890")
    private String socialId;

    @Schema(description = "이메일", example = "user@example.com")
    private String email;

    @Schema(description = "닉네임", example = "홍길동")
    private String nickname;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
    private String profileImage;
}