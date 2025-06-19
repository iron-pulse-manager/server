package com.gymmanager.domain.user.entity;

import com.gymmanager.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 사용자(사업장 사장님) 엔터티
 * 
 * 헬스장 관리 시스템을 사용하는 사업장 사장님의 계정 정보입니다.
 * 한 사용자는 여러 사업장을 관리할 수 있습니다.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    /**
     * 로그인 ID (중복 불가)
     */
    @Column(name = "user_id", unique = true, nullable = false, length = 50)
    @NotBlank(message = "사용자 ID는 필수입니다.")
    @Size(min = 4, max = 50, message = "사용자 ID는 4-50자 사이여야 합니다.")
    private String userId;

    /**
     * 암호화된 비밀번호
     */
    @Column(name = "password", nullable = false)
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    /**
     * 사용자 이름
     */
    @Column(name = "name", nullable = false, length = 100)
    @NotBlank(message = "이름은 필수입니다.")
    @Size(max = 100, message = "이름은 100자 이내여야 합니다.")
    private String name;

    /**
     * 이메일 주소 (중복 불가)
     */
    @Column(name = "email", unique = true, length = 255)
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Size(max = 255, message = "이메일은 255자 이내여야 합니다.")
    private String email;

    /**
     * 핸드폰 번호
     */
    @Column(name = "phone", length = 20)
    @Size(max = 20, message = "핸드폰 번호는 20자 이내여야 합니다.")
    private String phone;

    /**
     * 프로필 사진 URL
     */
    @Column(name = "profile_image_url", length = 500)
    @Size(max = 500, message = "프로필 이미지 URL은 500자 이내여야 합니다.")
    private String profileImageUrl;

    /**
     * 비즈니스 로직: 사용자가 활성 상태인지 확인
     */
    public boolean isActive() {
        // 추후 상태 필드 추가시 구현
        return true;
    }

    /**
     * 비즈니스 로직: 이메일이 설정되어 있는지 확인
     */
    public boolean hasEmail() {
        return email != null && !email.trim().isEmpty();
    }

    /**
     * 비즈니스 로직: 프로필 이미지가 설정되어 있는지 확인
     */
    public boolean hasProfileImage() {
        return profileImageUrl != null && !profileImageUrl.trim().isEmpty();
    }

    /**
     * toString 재정의 (보안상 비밀번호 제외)
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}