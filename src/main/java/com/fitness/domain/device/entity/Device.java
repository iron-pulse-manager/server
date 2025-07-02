package com.fitness.domain.device.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 디바이스(DEVICE) 엔티티
 * 직원, 회원 로그인 디바이스 정보를 관리하는 테이블
 */
@Entity
@Table(name = "device")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Device extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    private Long deviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    private DeviceType deviceType;

    @Column(name = "device_token", nullable = false)
    private String deviceToken;

    @Column(name = "push_token")
    private String pushToken;

    @Column(name = "app_version")
    private String appVersion;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "os_version")
    private String osVersion;
}