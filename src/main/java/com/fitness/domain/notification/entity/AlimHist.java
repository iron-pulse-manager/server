package com.fitness.domain.notification.entity;

import com.fitness.common.BaseEntity;
import com.fitness.common.enums.AlimType;
import com.fitness.common.enums.DeviceType;
import com.fitness.common.enums.UserType;
import com.fitness.domain.business.entity.Business;
import com.fitness.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 알림 내역 엔티티
 * 시스템 내 모든 알림 발송 및 수신 내역을 관리
 */
@Entity
@Table(name = "alim_hist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlimHist extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alim_hist_id")
    private Long alimHistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    private DeviceType deviceType; // 디바이스 유형

    @Enumerated(EnumType.STRING)
    @Column(name = "alim_type", nullable = false)
    private AlimType alimType; // 알림 유형

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User senderId; // 발신자 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipientId; // 수신자 ID

    @Column(name = "title", length = 200)
    private String title; // 알림 제목

    @Column(name = "content", columnDefinition = "TEXT")
    private String content; // 알림 내용

    @Column(name = "send_date", nullable = false)
    private LocalDateTime sendDate; // 발송일시

    @Column(name = "read_date")
    private LocalDateTime readDate; // 수신일시

}