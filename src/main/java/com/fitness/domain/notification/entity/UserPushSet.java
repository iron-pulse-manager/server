package com.fitness.domain.notification.entity;

import com.fitness.common.BaseEntity;
import com.fitness.common.enums.PushType;
import com.fitness.common.enums.UserType;
import com.fitness.domain.business.entity.Business;
import com.fitness.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * 유저 푸시 설정 엔티티
 * 사용자별 푸시 알림 수신 설정을 관리
 */
@Entity
@Table(name = "user_push_set")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPushSet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_push_set_id")
    private Long userPushSetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId; // 사용자 ID

    @Column(name = "active_yn", nullable = false)
    @Builder.Default
    private boolean activeYn = true; // 푸시 활성화 여부
}