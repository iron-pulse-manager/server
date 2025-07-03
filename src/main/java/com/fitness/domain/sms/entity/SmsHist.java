package com.fitness.domain.sms.entity;

import com.fitness.common.BaseEntity;
import com.fitness.domain.business.entity.Business;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 문자 발송 이력(SMS_HIST) 엔티티
 * 문자 발송 내역을 관리하는 테이블
 */
@Entity
@Table(name = "sms_hist")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SmsHist extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sms_hist_id")
    private Long smsHistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false)
    private MessageType messageType;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "recipient_cnt", nullable = false)
    private Integer recipientCnt;

    @Column(name = "sent_date")
    private LocalDateTime sentDate;

    @OneToMany(mappedBy = "smsHist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SmsRecipient> recipients = new ArrayList<>();

}