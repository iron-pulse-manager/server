# 🏋️ 피트니스 관리 시스템 - 시스템 아키텍처 설계 (MVP)

## 📋 목차
1. [시스템 개요](#시스템-개요)
2. [Layered Architecture (계층형 아키텍처)](#layered-architecture-계층형-아키텍처)
3. [패키지 구조](#패키지-구조)
4. [핵심 엔티티 설계](#핵심-엔티티-설계)
5. [데이터베이스 설계](#데이터베이스-설계)
6. [API 설계 원칙](#api-설계-원칙)
7. [보안 및 인증](#보안-및-인증)
8. [성능 및 확장성](#성능-및-확장성)
9. [배포 전략](#배포-전략)

---

## 🎯 시스템 개요

### **비즈니스 목적**
헬스장, 필라테스 등 체육시설업 사업장을 위한 **SaaS형 통합 관리 솔루션 MVP**
- **웹**: 사업장 매출/회원/직원/일정 관리
- **앱**: 직원용 회원 관리 및 회원용 서비스

### **MVP 핵심 가치**
1. **빠른 시장 진입**: 단순한 구조로 개발 속도 향상
2. **검증 중심**: 핵심 기능에 집중하여 사용자 피드백 수집
3. **확장 가능**: 향후 성장에 따른 리팩토링 용이성
4. **유지보수성**: 직관적인 구조로 개발자 온보딩 시간 단축

### **사용자 유형별 권한**
| 사용자 | 플랫폼 | 주요 기능 | 권한 범위 |
|--------|-------|-----------|-----------|
| **사장님** | 웹 | 매출관리, 직원관리, 통계 | 전체 사업장 |
| **직원** | 앱 | 담당회원 관리, 운동일지, 식단관리 | 담당 회원만 |
| **회원** | 앱 | 레슨일정, 운동일지, 체중관리 | 개인 데이터만 |

---

## 🏗️ Layered Architecture (계층형 아키텍처)

### **MVP용 3계층 구조**

```
┌─────────────────────────────────────────┐
│          Presentation Layer            │
│    Controllers, DTOs, Exception        │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│           Business Layer               │
│         Services, Validation           │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│          Data Access Layer             │
│      Repositories, Entities            │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│        Infrastructure Layer            │
│     Database, Security, Config         │
└─────────────────────────────────────────┘
```

### **계층별 책임**

| 계층 | 책임 | 주요 컴포넌트 |
|------|------|---------------|
| **Presentation** | HTTP 요청/응답 처리, 데이터 변환 | Controllers, DTOs, Validators |
| **Business** | 비즈니스 로직, 트랜잭션 관리 | Services, Business Rules |
| **Data Access** | 데이터 영속성, 쿼리 최적화 | Repositories, Entities |
| **Infrastructure** | 외부 시스템 연동, 설정 관리 | Config, Security, Utils |

### **장점 (MVP 관점)**
✅ **단순성**: 개발자가 쉽게 이해할 수 있는 구조  
✅ **개발 속도**: 복잡한 도메인 경계 설정 불필요  
✅ **디버깅 용이**: 직선적인 호출 구조로 문제 추적 용이  
✅ **팀 협업**: 계층별 역할 분담이 명확  

---

## 📦 패키지 구조

### **단일 모듈 구조**

```
📁 src/main/java/com/fitness/
├── 📁 config/                    # 설정 클래스
│   ├── JpaConfig.java
│   ├── SecurityConfig.java
│   ├── SwaggerConfig.java
│   └── RedisConfig.java
├── 📁 controller/                # REST Controllers
│   ├── owner/
│   ├── employee/
│   ├── member/
│   ├── business/
│   ├── product/
│   ├── payment/
│   └── ... (도메인별)
├── 📁 service/                   # 비즈니스 로직
│   ├── owner/
│   ├── employee/
│   ├── member/
│   └── ... (도메인별)
├── 📁 repository/                # 데이터 접근
│   ├── owner/
│   ├── employee/
│   ├── member/
│   └── ... (도메인별)
├── 📁 entity/                    # JPA 엔티티
│   ├── owner/
│   ├── employee/
│   ├── member/
│   └── ... (도메인별)
├── 📁 dto/                       # 데이터 전송 객체
│   ├── owner/
│   ├── employee/
│   ├── member/
│   └── ... (도메인별)
├── 📁 security/                  # 보안 관련
│   ├── JwtTokenProvider.java
│   ├── CustomUserDetails.java
│   └── SecurityUtils.java
├── 📁 exception/                 # 예외 처리
│   ├── BusinessException.java
│   ├── ErrorCode.java
│   └── GlobalExceptionHandler.java
└── 📁 util/                      # 공통 유틸리티
    ├── ApiResponse.java
    ├── DateUtils.java
    └── ValidationUtils.java
```

### **패키지별 네이밍 규칙**
- **Controller**: `{Domain}Controller.java`
- **Service**: `{Domain}Service.java`  
- **Repository**: `{Domain}Repository.java`
- **Entity**: `{Domain}.java`
- **DTO**: `{Domain}{Purpose}Dto.java`

---

## 🎯 핵심 도메인 설계

### **1. 사용자 관리 도메인**

#### **User (통합 사용자)**
```java
@Entity
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    
    @Enumerated(EnumType.STRING)
    private UserType userType;      // OWNER, EMPLOYEE, MEMBER
    
    private String name;
    private LocalDate birthday;
    private Gender gender;
    private UserStatus status;      // ACTIVE, INACTIVE
    private String phoneNumber;
    private String address;
    
    // 1:N 관계 - 사용자별 인증 방식들
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Auth> authList = new ArrayList<>();
    
    // 비즈니스 메서드
    public boolean isOwner();
    public boolean isEmployee();
    public boolean isMember();
}
```

#### **Auth (인증 정보)**
```java
@Entity
public class Auth {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    private SocialProvider provider;  // NONE, KAKAO, APPLE
    
    // 일반 로그인용 (사장님)
    private String username;
    private String password;
    
    // 소셜 로그인용 (직원/회원)
    private String socialId;
    private String email;
    private String nickname;
    private String accessToken;
    private String refreshToken;
    
    // 비즈니스 메서드
    public boolean isRegularAuth();
    public boolean isSocialAuth();
    public boolean isKakaoAuth();
    public boolean isAppleAuth();
}
```

### **2. 결제 및 상품 도메인**

#### **Payment (통합 결제)**
```java
@Entity
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long businessId;        // 소속 사업장
    private Long memberId;          // 결제 회원
    private Long productId;         // 구매 상품
    private Long consultantId;      // 상담 직원
    private Long trainerId;         // 담당 강사
    
    private BigDecimal productPrice;      // 상품 금액
    private BigDecimal actualPrice;       // 실제 결제 금액
    private BigDecimal instructorCommission; // 강사 매출 실적
    private BigDecimal outstandingAmount; // 미수금
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; // CARD, CASH, TRANSFER
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus; // PENDING, COMPLETED, CANCELLED
    
    private LocalDate serviceStartDate;  // 서비스 시작일
    private LocalDate serviceEndDate;    // 서비스 종료일
    
    // 비즈니스 메서드
    public boolean isActive();
    public boolean isExpiringSoon(int days);
    public void complete();
    public void cancel(String reason);
}
```

#### **Product (상품)**
```java
@Entity
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long businessId;        // 소속 사업장
    
    @Enumerated(EnumType.STRING)
    private ProductType productType; // MEMBERSHIP, PERSONAL_TRAINING, LOCKER, OTHERS
    
    private BigDecimal price;       // 가격
    private Integer duration;       // 기간 (일 단위)
    private Integer sessionCount;   // 횟수 (개인레슨용)
    
    // 비즈니스 메서드
    public LocalDate calculateEndDate(LocalDate startDate);
    public boolean isPersonalTraining();
    public boolean requiresTrainer();
}
```

---

## 🗄️ 데이터베이스 설계

### **핵심 설계 원칙**
1. **정규화**: 3NF까지 정규화, 성능 필요시 비정규화
2. **인덱스 전략**: 조회 패턴 기반 복합 인덱스 설계
3. **외래키 제약**: 데이터 무결성 보장, 성능 고려한 CASCADE 설정
4. **파티셔닝**: 결제 데이터는 연도별 파티셔닝

### **주요 테이블 관계**
```sql
-- 핵심 관계
owners 1:N businesses 1:N employees
owners 1:N businesses 1:N products
members N:M businesses (through business_members)
members 1:N payments N:1 products

-- 통계용 인덱스
CREATE INDEX idx_payments_business_date ON payments(business_id, payment_date);
CREATE INDEX idx_payments_trainer_date ON payments(trainer_id, payment_date);
```

### **성능 최적화**
- **읽기 전용 복제본**: 통계 쿼리용 별도 DB
- **캐싱 전략**: Redis를 통한 사업장/상품 정보 캐싱
- **배치 처리**: 통계 데이터 일배치 갱신

---

## 🔐 보안 및 인증

### **통합 인증 시스템 구조**

#### **User ↔ Auth 관계 (1:N)**
```java
User (1) ← → (N) Auth
```
- **1명의 사용자**가 **여러 인증 방식**을 가질 수 있음
- 사장님: 일반 로그인 1개 (provider = NONE)
- 직원/회원: 카카오 + 애플 등 복수 소셜 로그인 (provider = KAKAO, APPLE)

### **인증 체계**
| 사용자 타입 | 인증 방식 | 토큰 유효기간 | 권한 관리 |
|-------------|-----------|---------------|-----------|
| **사장님** | ID/PW + JWT | 8시간 | Role-based |
| **직원** | 소셜 로그인 (카카오/애플) + JWT | 30일 | Business-scoped |
| **회원** | 소셜 로그인 (카카오/애플) + JWT | 30일 | Member-scoped |

### **보안 정책**
1. **비밀번호**: BCrypt 해싱, 최소 8자리
2. **API 보안**: Rate Limiting, CORS 설정
3. **데이터 보호**: 개인정보 암호화, 로깅 마스킹
4. **권한 검증**: AOP 기반 메서드 레벨 권한 체크

### **권한 체계**
```java
@PreAuthorize("hasRole('OWNER') and @securityService.hasBusinessAccess(#businessId)")
public MemberResponse getMember(Long businessId, Long memberId) {
    // 사장님이 본인 사업장의 회원만 조회 가능
}

@PreAuthorize("hasRole('EMPLOYEE') and @securityService.isTrainerOf(#memberId)")
public void updateWorkoutLog(Long memberId, WorkoutLogCommand command) {
    // 직원이 담당 회원의 운동일지만 수정 가능
}
```

---

## ⚡ 성능 및 확장성

### **성능 목표**
- **응답시간**: 95% 요청 < 500ms
- **처리량**: 1,000 RPS (peak time)
- **가용성**: 99.9% uptime
- **동시사용자**: 10,000명

### **확장성 전략**

#### **수평 확장**
```yaml
# Kubernetes 배포 예시
apiVersion: apps/v1
kind: Deployment
metadata:
  name: fitness-api
spec:
  replicas: 3
  template:
    spec:
      containers:
      - name: api
        image: fitness/api:latest
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
```

#### **캐싱 전략**
```java
@Cacheable(value = "business", key = "#businessId")
public BusinessResponse getBusiness(Long businessId) {
    // 사업장 정보 캐싱 (TTL: 1시간)
}

@Cacheable(value = "products", key = "#businessId")
public List<ProductResponse> getBusinessProducts(Long businessId) {
    // 상품 목록 캐싱 (TTL: 30분)
}
```

#### **데이터베이스 최적화**
1. **읽기 전용 복제본**: 통계/조회용
2. **연결 풀링**: HikariCP 설정
3. **쿼리 최적화**: N+1 문제 해결, 배치 처리
4. **인덱스 최적화**: 실행 계획 기반 인덱스 설계

---

## 🚀 배포 전략

### **환경별 구성**
| 환경 | 목적 | 인스턴스 | DB | 모니터링 |
|------|------|----------|----|---------| 
| **Local** | 개발 | Docker Compose | H2/MySQL | 로컬 로그 |
| **Development** | 통합 테스트 | 1 Pod | MySQL | ELK Stack |
| **Staging** | 운영 전 검증 | 2 Pods | MySQL (Primary-Replica) | Prometheus + Grafana |
| **Production** | 실서비스 | 3+ Pods | MySQL Cluster | 풀스택 모니터링 |

### **CI/CD 파이프라인**
```yaml
# GitHub Actions 워크플로우
name: CI/CD Pipeline
on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Setup JDK 17
        uses: actions/setup-java@v3
      - name: Run Tests
        run: ./gradlew test
      - name: SonarQube Analysis
        run: ./gradlew sonarqube
  
  deploy:
    needs: test
    if: github.ref == 'refs/heads/main'
    runs-on: ubuntu-latest
    steps:
      - name: Build Docker Image
        run: docker build -t fitness/api:${{ github.sha }} .
      - name: Deploy to Kubernetes
        run: kubectl apply -f k8s/
```

### **무중단 배포**
1. **Blue-Green 배포**: 전체 환경 교체
2. **Rolling Update**: Pod 단위 점진적 교체
3. **Canary 배포**: 트래픽 일부만 신버전으로

### **모니터링 및 알림**
```yaml
# Prometheus 메트릭 수집
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true

# 주요 메트릭
- API 응답시간 (histogram)
- 비즈니스 메트릭 (결제 성공률, 가입률)
- 시스템 메트릭 (CPU, Memory, DB Connection)
```

---

## 📊 개발 로드맵 (예상 일정)

### **Phase 1: 핵심 기능 개발 (8주)**
- Week 1-2: 사용자 관리 (Owner, Employee, Member)
- Week 3-4: 사업장 및 상품 관리
- Week 5-6: 결제 시스템 구현
- Week 7-8: 기본 회원 관리 기능

### **Phase 2: 운영 기능 확장 (6주)**
- Week 9-10: 일정 및 출석 관리
- Week 11-12: 락커 및 일일권 관리
- Week 13-14: 문자 발송 및 통계

### **Phase 3: 앱 특화 기능 (4주)**
- Week 15-16: 운동일지 및 식단 관리
- Week 17-18: 체중 관리 및 커뮤니티

### **Phase 4: 최적화 및 배포 (2주)**
- Week 19-20: 성능 최적화, 배포, 모니터링

---

## 🔧 기술 스택

### **Backend**
- **언어**: Java 17
- **프레임워크**: Spring Boot 3.2, Spring Security + JWT, Spring Data JPA
- **빌드 도구**: Gradle
- **데이터베이스**: MySQL 8.0 / H2 (테스트)
- **인증**: BCryptPasswordEncoder + JWT + OAuth2
- **쿼리**: QueryDSL (동적 쿼리)

### **Infrastructure**
- **컨테이너**: Docker, Kubernetes
- **클라우드**: AWS (EKS, RDS, ElastiCache)
- **모니터링**: Prometheus, Grafana, ELK Stack
- **CI/CD**: GitHub Actions, ArgoCD

### **개발 도구**
- **IDE**: IntelliJ IDEA
- **버전 관리**: Git, GitHub
- **API 문서**: Swagger/OpenAPI 3.0
- **테스트**: JUnit 5, TestContainers, WireMock

---

## 📈 성공 지표 (KPI)

### **기술적 지표**
- **성능**: 평균 응답시간 < 300ms
- **안정성**: 99.9% 가용성
- **확장성**: 10배 트래픽 증가 대응
- **보안**: 취약점 0건 유지

### **비즈니스 지표**
- **사용자 만족도**: 4.5/5.0 이상
- **기능 활용률**: 핵심 기능 80% 이상 사용
- **데이터 정확성**: 99.95% 이상
- **지원 요청**: 월 평균 10건 이하

이 문서는 시스템의 전체적인 아키텍처 방향성을 제시하며, 개발 진행에 따라 지속적으로 업데이트될 예정입니다.