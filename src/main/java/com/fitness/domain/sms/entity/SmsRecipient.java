package com.fitness.domain.sms.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 문자 수신자(SMS_RECIPIENT) 엔티티
 * 문자 수신자 정보를 관리하는 테이블
 */
@Entity
@Table(name = "sms_recipient")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SmsRecipient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sms_recipient_id")
    private Long smsRecipientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sms_hist_id", nullable = false)
    private SmsHist smsHist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_user_id")
    private User recipientUser;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "send_status", nullable = false)
    private SendStatus sendStatus;

}