# Iron Pulse Manager Server

헬스장, 필라테스 등 체육시설업을 위한 SaaS형 관리 시스템

## 📋 프로젝트 개요

**Iron Pulse Manager**는 체육시설업 사업장을 대상으로 웹 관리자 시스템과 직원/회원용 모바일 앱을 제공하는 통합 관리 솔루션입니다.

- **웹**: 사업장 매출관리, 회원관리, 직원관리, 일정관리
- **직원앱**: 담당 회원 관리, 운동일지 작성, 식단 피드백
- **회원앱**: 개인 정보 관리, 운동일지 조회, 식단 기록, 체중 관리

## 🛠️ 기술 스택

- **Language**: Java 17
- **Framework**: Spring Boot 3.2
- **Security**: Spring Security + JWT
- **Database**: MySQL 8.0 / H2 (테스트)
- **ORM**: JPA + QueryDSL
- **Build Tool**: Gradle 8.5
- **Authentication**: 
  - 일반 로그인 (사장님): ID/Password + BCrypt
  - 소셜 로그인 (직원/회원): 카카오/애플 OAuth2

## 🚀 빠른 시작

### 1. 환경 구성

```bash
# 저장소 클론
git clone https://github.com/iron-pulse-manager/server.git
cd server

# 로컬 환경 설정
./scripts/setup-local.sh

# Docker 환경 시작
docker-compose up -d
```

### 2. 애플리케이션 실행

```bash
# 개발 서버 실행
./gradlew bootRun
```

### 3. 접속 확인

- **애플리케이션**: http://localhost:8080
- **API 문서**: http://localhost:8080/swagger-ui.html
- **헬스체크**: http://localhost:8080/actuator/health

## 📁 프로젝트 구조

```
src/main/java/com/fitness/
├── FitnessManagementApplication.java
├── common/                 # 공통 기능
│   ├── config/
│   ├── exception/
│   └── security/
└── domain/                # 도메인별 모듈
    ├── auth/              # 인증
    ├── user/              # 사용자 (사장님, 직원, 회원 통합)
    ├── business/          # 사업장 및 사업장-사용자 관계
    ├── product/           # 상품
    ├── payment/           # 결제
    ├── schedule/          # 일정
    ├── statistics/        # 통계
    ├── community/         # 커뮤니티
    ├── locker/            # 락커룸
    ├── daypass/           # 일일권
    ├── workout/           # 운동일지
    ├── diet/              # 식단
    ├── weight/            # 체중 관리
    ├── message/           # 메시지
    ├── attendance/        # 출석
    ├── expense/           # 지출 관리
    ├── commission/        # 수수료/실적
    ├── membership/        # 회원권
    └── notification/      # 알림
```

## 🔐 사용자 권한 및 인증 시스템

### 통합 사용자 관리
모든 사용자(사장님, 직원, 회원)는 **User** 엔티티로 통합 관리되며, 사업장별 역할은 **BusinessEmployee**, **BusinessMember** 관계 엔티티로 구분됩니다.

| 사용자 유형 | 플랫폼 | 인증 방식 | 주요 기능 | 관계 엔티티 |
|-------------|--------|-----------|-----------| -------------|
| **사장님** | 웹 | 일반 로그인 (ID/PW) | 전체 사업장 관리, 매출 통계, 직원/회원 관리 | Business 소유자 |
| **직원** | 모바일앱 | 소셜 로그인 (카카오/애플) | 담당 회원 관리, 운동일지 작성, 식단 피드백 | BusinessEmployee |
| **회원** | 모바일앱 | 소셜 로그인 (카카오/애플) | 개인 정보 조회, 운동 기록, 식단 관리 | BusinessMember |

### 인증 시스템 구조

#### User ↔ Auth 관계 (1:N)
```java
User (1) ←→ (N) Auth
```
- **1명의 사용자**가 **여러 인증 방식**을 가질 수 있음
- 사장님: 일반 로그인 1개
- 직원/회원: 카카오 + 애플 등 복수 소셜 로그인 가능

#### Auth 엔티티 구조
```java
@Entity
public class Auth {
    private Long authId;
    private SocialProvider provider;  // NONE, KAKAO, APPLE
    private String username;          // 일반 로그인용 (사장님)
    private String password;          // 일반 로그인용 (사장님)
    private String socialId;          // 소셜 고유 ID (직원/회원)
    private String email;             // 소셜에서 받은 이메일
    private String nickname;          // 소셜에서 받은 닉네임
    private String accessToken;       // 소셜 액세스 토큰
    private String refreshToken;      // 소셜 리프레시 토큰
}
```

#### 인증 엔드포인트
```
POST /api/auth/login                    # 일반 로그인 (사장님)
POST /api/auth/social/kakao             # 카카오 로그인 (직원/회원)
POST /api/auth/social/apple             # 애플 로그인 (직원/회원)
POST /api/auth/social/{provider}/link   # 소셜 계정 연동 추가
```

## 🧪 테스트 및 빌드

```bash
# 전체 테스트 실행
./gradlew test

# 빌드
./gradlew build
```

## ⚙️ 환경 설정

### 데이터베이스 환경별 설정

#### 테스트 환경 (application-test.yml)
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
```

#### 로컬 개발 환경 (application-local.yml)
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fitness_db?createDatabaseIfNotExist=true
    username: root
    password: 1234
  jpa:
    hibernate:
      ddl-auto: create-drop
```

#### 프로덕션 환경 (application-prod.yml)
```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate
```

### 프로파일
- `test`: 테스트 환경 (H2 메모리 DB)
- `local`: 로컬 개발 환경 (로컬 MySQL)
- `prod`: 운영 환경 (AWS RDS)

## 📊 주요 기능

### 웹 관리자
- **권한별 대시보드**: 마스터/일반 권한 구분
- **회원 관리**: 이용권 등록, 출석 현황, 결제 내역
- **직원 관리**: 가입 승인, 실적 조회
- **상품 관리**: 회원권, 개인레슨권, 락커권 등
- **통계**: 매출, 회원, 직원 실적 분석

### 직원 앱
- **회원 관리**: 담당 회원 목록, 상세 정보
- **운동일지**: 운동 기록 작성 및 관리
- **식단 관리**: 회원 식단 피드백
- **일정 관리**: 개인 레슨 일정

### 회원 앱
- **개인 정보**: 이용권 현황, 출석 기록
- **운동 기록**: 운동일지 조회, 질문 작성
- **식단 관리**: 일별 식단 기록
- **체중 관리**: 목표/현재 체중, 사진 기록

## 🔧 개발 가이드

### 코딩 규칙
- Java 17+ 문법 사용
- Spring Boot 3.2 기반 개발
- QueryDSL을 활용한 동적 쿼리 작성
- 도메인 중심 설계 (DDD) 적용

### Git 브랜치 전략
- `main`: 운영 배포 브랜치
- `develop`: 개발 통합 브랜치
- `feature/*`: 기능 개발 브랜치

## 📞 문의

프로젝트 관련 문의사항은 GitHub Issues를 통해 등록해주세요.