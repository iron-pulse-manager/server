package com.fitness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 🏋️ 피트니스 관리 시스템 메인 애플리케이션
 * 
 * MVP용 Layered Architecture 기반 단일 모듈 구조
 * - Presentation Layer: Controllers, DTOs
 * - Business Layer: Services
 * - Data Access Layer: Repositories, Entities
 * - Infrastructure Layer: Configuration, Security, Utils
 */
@SpringBootApplication
@EnableJpaAuditing
public class FitnessManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitnessManagementApplication.class, args);
    }
}