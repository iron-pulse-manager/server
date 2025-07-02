package com.fitness.domain.admin.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.auth.entity.Auth;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 관리자(ADMIN) 엔티티
 * 시스템 관리자 정보를 관리하는 테이블
 */
@Entity
@Table(name = "admin")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AdminStatus status;

    @Column(name = "auth_type")
    private AuthType authType;

}