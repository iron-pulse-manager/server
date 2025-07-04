package com.fitness.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 요청 DTO
 */
@Getter
@Setter
@Schema(description = "로그인 요청")
public class LoginRequest {

    @NotBlank(message = "사용자명은 필수입니다.")
    @Schema(description = "사용자명", example = "admin")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Schema(description = "비밀번호", example = "password123")
    private String password;
}