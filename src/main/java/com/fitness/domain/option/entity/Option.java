package com.fitness.domain.option.entity;

import com.fitness.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 설정(OPTION) 엔티티
 * 시스템 설정 정보를 관리하는 테이블
 */
@Entity
@Table(name = "option")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Option extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long optionId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "use_yn", nullable = false)
    private boolean useYn = true;

}