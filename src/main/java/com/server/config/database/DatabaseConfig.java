package com.gymmanager.config.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 데이터베이스 설정 클래스
 * 
 * MySQL 데이터베이스 연결 및 JPA/Hibernate 설정을 담당합니다.
 * Multi-tenant 아키텍처를 지원하는 데이터베이스 설정을 제공합니다.
 */
@Configuration
@EnableJpaRepositories(
    basePackages = "com.gymmanager.domain.*.repository",
    entityManagerFactoryRef = "entityManagerFactory",
    transactionManagerRef = "transactionManager"
)
@RequiredArgsConstructor
@Slf4j
public class DatabaseConfig {

    /**
     * HikariCP 데이터베이스 커넥션 풀 설정
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();
        
        // 기본 설정은 application.yml에서 주입
        config.setPoolName("GymManagerHikariPool");
        config.setLeakDetectionThreshold(60000);
        config.setConnectionTestQuery("SELECT 1");
        
        log.info("HikariCP configuration initialized");
        return config;
    }

    /**
     * Primary DataSource 설정
     */
    @Bean
    @Primary
    public DataSource dataSource(HikariConfig hikariConfig) {
        // DataSource URL, username, password는 application.yml에서 자동 주입
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        
        log.info("Primary DataSource created with pool size: {}", 
                dataSource.getMaximumPoolSize());
        
        return dataSource;
    }

    /**
     * JPA EntityManagerFactory 설정
     */
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource) {
        
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.gymmanager.domain");
        
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        
        // Hibernate 속성 설정
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.setProperty("hibernate.show_sql", "false");
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty("hibernate.use_sql_comments", "true");
        properties.setProperty("hibernate.jdbc.batch_size", "25");
        properties.setProperty("hibernate.order_inserts", "true");
        properties.setProperty("hibernate.order_updates", "true");
        properties.setProperty("hibernate.jdbc.batch_versioned_data", "true");
        
        // Multi-tenant 설정
        properties.setProperty("hibernate.multiTenancy", "DISCRIMINATOR");
        properties.setProperty("hibernate.tenant_identifier_resolver", 
                "com.gymmanager.config.tenant.TenantIdentifierResolver");
        
        // 캐시 설정
        properties.setProperty("hibernate.cache.use_second_level_cache", "true");
        properties.setProperty("hibernate.cache.use_query_cache", "true");
        properties.setProperty("hibernate.cache.region.factory_class", 
                "org.hibernate.cache.jcache.JCacheRegionFactory");
        
        em.setJpaProperties(properties);
        
        log.info("EntityManagerFactory configured with Multi-tenant support");
        return em;
    }

    /**
     * JPA 트랜잭션 매니저 설정
     */
    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(
            LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        
        log.info("JpaTransactionManager configured");
        return transactionManager;
    }
}