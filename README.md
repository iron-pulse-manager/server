# 헬스장 관리 SaaS REST API 서버

헬스장, 필라테스 등 피트니스 사업장 관리를 위한 Multi-tenant SaaS REST API 서버입니다.

## 🏗️ 시스템 아키텍처

```
┌─────────────────────────────────────────────────────────────┐
│                Spring Boot REST API Server                 │
│              (Stateless Backend Only)                      │
│        - JWT 인증                                           │
│        - REST API 엔드포인트                                 │
│        - Multi-tenant 지원                                  │
│        - CORS 설정                                          │
└─────────────────────┬───────────────────────────────────────┘
                      │ HTTP REST API 통신
        ┌─────────────┼─────────────┐
        │             │             │
┌───────▼──────┐ ┌───▼───┐ ┌───────▼──────┐
│ React 웹서버   │ │ 직원 앱 │ │   회원 앱    │
│ (별도 서버)    │ │(Flutter)│ │  (Flutter)   │
│ - React.js    │ │         │ │             │
│ - Node.js     │ │ 모바일앱 │ │   모바일앱    │
│ - 관리자 UI   │ │         │ │             │
└─────────────────┘ └───────┘ └─────────────┘
```

## 🚀 주요 특징

### Multi-tenant 아키텍처
- 사업장별 데이터 완전 분리
- 자동 사업장 컨텍스트 관리
- 사업장별 권한 제어

### 클라이언트 지원
- **웹 관리자** (React): 사업장 관리, 회원 관리, 통계 등
- **직원 앱** (Flutter): 매출 조회, 회원 관리, 일정 관리
- **회원 앱** (Flutter): 출석 체크, 예약, 개인 정보 관리

### REST API 설계
- 표준화된 응답 형식
- 클라이언트별 API 엔드포인트 분리
- JWT 기반 인증
- 요청 추적 및 로깅

## 🛠️ 기술 스택

- **Backend**: Java 17, Spring Boot 3.2.x
- **Database**: MySQL 8.0+, JPA/Hibernate
- **Authentication**: JWT, Spring Security
- **Documentation**: Swagger/OpenAPI 3
- **Caching**: Redis
- **Build**: Gradle

## 📁 프로젝트 구조

```
src/main/java/com/gymmanager/
├── api/                          # REST API 컨트롤러
│   ├── common/                   # 공통 API
│   ├── web/                      # 웹 관리자용 API
│   ├── staff/                    # 직원 앱용 API
│   └── member/                   # 회원 앱용 API
├── common/                       # 공통 컴포넌트
│   ├── dto/                      # 데이터 전송 객체
│   ├── entity/                   # 기본 엔터티
│   ├── enums/                    # 열거형
│   ├── exception/                # 예외 처리
│   ├── repository/               # 기본 리포지토리
│   └── service/                  # 기본 서비스
├── config/                       # 설정 클래스
│   ├── database/                 # 데이터베이스 설정
│   ├── security/                 # 보안 설정
│   └── tenant/                   # Multi-tenant 설정
└── domain/                       # 도메인별 구조
    ├── user/                     # 사용자 도메인
    ├── business/                 # 사업장 도메인
    ├── member/                   # 회원 도메인
    ├── product/                  # 상품 도메인
    └── ...
```

## 🔧 환경 설정

### 필수 요구사항
- Java 17+
- MySQL 8.0+
- Redis (선택사항)

### 로컬 개발 환경 설정

1. **데이터베이스 설정**
```sql
CREATE DATABASE gym_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'gym_manager'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON gym_manager.* TO 'gym_manager'@'localhost';
```

2. **환경변수 설정** (application-local.yml)
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gym_manager
    username: gym_manager
    password: password
  
  data:
    redis:
      host: localhost
      port: 6379
```

3. **애플리케이션 실행**
```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

## 📚 API 문서

### Swagger UI
- **로컬**: http://localhost:8080/swagger-ui/index.html
- **개발**: https://api-dev.gymmanager.com/swagger-ui/index.html

### API 엔드포인트 구조

#### 공통 API (`/api/v1/common/`)
- 인증, 파일 업로드, 헬스체크 등

#### 웹 관리자 API (`/api/v1/web/`)
- 사업장 관리
- 회원 관리
- 통계/매출 관리
- 직원 관리

#### 직원 앱 API (`/api/v1/staff/`)
- 사업장 가입 신청
- 담당 회원 관리
- 개인 매출 조회
- 일정 관리

#### 회원 앱 API (`/api/v1/member/`)
- 개인 정보 관리
- 출석 체크
- 예약 시스템

## 🔐 인증 및 권한

### JWT 토큰 구조
```json
{
  "sub": "user-id",
  "clientType": "WEB_ADMIN",
  "userRole": "BUSINESS_OWNER",
  "businessId": 123,
  "permissions": ["MANAGE_BUSINESS", "VIEW_STATISTICS"],
  "iat": 1234567890,
  "exp": 1234567890
}
```

### 사용자 역할
- **BUSINESS_OWNER**: 사업장 사장님 (모든 권한)
- **STAFF**: 직원/트레이너 (제한된 권한)
- **MEMBER**: 일반 회원 (개인 정보만)

### 클라이언트 타입
- **WEB_ADMIN**: 웹 관리자
- **STAFF_APP**: 직원 앱
- **MEMBER_APP**: 회원 앱

## 📊 모니터링

### 헬스체크
- **URL**: `/api/v1/common/health`
- **Actuator**: `/actuator/health`

### 로깅
- 요청 추적 ID 자동 생성
- 클라이언트별 접근 로그
- 에러 추적 및 알림

## 🚀 배포

### 환경별 설정
- **local**: 로컬 개발 환경
- **dev**: 개발 서버
- **prod**: 프로덕션 서버

### Docker 배포
```bash
# 빌드
./gradlew build

# Docker 이미지 생성
docker build -t gym-manager-api .

# 실행
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=mysql-server \
  -e DB_USERNAME=gym_manager \
  -e DB_PASSWORD=secure_password \
  gym-manager-api
```

## 🤝 개발 가이드

### 새로운 API 추가 시
1. 클라이언트 타입 확인 (web/staff/member)
2. 적절한 패키지에 컨트롤러 생성
3. BaseRestController 상속
4. Swagger 어노테이션 추가
5. 권한 설정 (@PreAuthorize)

### Multi-tenant 엔터티 생성 시
1. TenantBaseEntity 상속
2. TenantRepository 인터페이스 사용
3. TenantService 상속하여 서비스 구현

## 📞 문의

- **개발팀**: dev@gymmanager.com
- **기술지원**: support@gymmanager.com
- **문서**: https://docs.gymmanager.com