# Fitness Management System

체육시설업 사업장을 위한 SaaS형 관리 솔루션

## 🏗️ 아키텍처

- **모듈러 모놀리식 아키텍처**: 도메인별 모듈 분리로 확장성과 유지보수성 확보
- **Java 17 + Spring Boot 3.2**: 최신 기술 스택 활용
- **MySQL + Redis**: 안정적인 데이터 저장 및 캐싱
- **JWT 인증**: 보안성과 확장성을 고려한 토큰 기반 인증

## 📦 모듈 구조

```
fitness/
├── src/main/java/com/fitness/Application.java  # 메인 애플리케이션
├── src/main/resources/                         # 설정 파일들
├── common/                                     # 공통 기능 (인증, 예외처리, 유틸리티)
├── user/                                       # 사용자(사장님) 관리
├── member/                                     # 회원 관리
├── business/                                   # 사업장 관리
├── employee/                                   # 직원 관리
├── product/                                    # 상품 관리
├── payment/                                    # 결제 관리
├── schedule/                                   # 일정 관리
├── statistics/                                 # 통계 관리
└── community/                                  # 커뮤니티 관리
```

## 🚀 빠른 시작

### 사전 요구사항

- Java 17 이상
- Docker & Docker Compose
- Git

### 로컬 환경 셋업

```bash
# 저장소 클론
git clone https://github.com/your-org/fitness-management.git
cd fitness-management

# 로컬 환경 자동 셋업
./scripts/setup-local.sh

# 애플리케이션 실행
./gradlew bootRun
```

### Docker를 이용한 전체 환경 실행

```bash
# 전체 서비스 시작
docker-compose up -d

# 로그 확인
docker-compose logs -f app
```

## 🛠️ 개발 환경

### 필수 도구

- **IDE**: IntelliJ IDEA (권장)
- **Java**: OpenJDK 17
- **Build Tool**: Gradle 8.5
- **Database**: MySQL 8.0
- **Cache**: Redis 7.0

### 환경 설정

#### application.yml 설정
```yaml
spring:
  profiles:
    active: local  # local, dev, prod
```

#### 환경별 설정 파일
- `application-local.yml`: 로컬 개발 환경
- `application-dev.yml`: 개발 서버 환경  
- `application-prod.yml`: 운영 환경

### 데이터베이스 접속 정보

#### 로컬 환경
- **Host**: localhost:3306
- **Database**: fitness_db
- **Username**: fitness_user
- **Password**: fitness_password

## 🧪 테스트

```bash
# 전체 테스트 실행
./gradlew test

# 특정 모듈 테스트
./gradlew :user:test

# 테스트 리포트 생성
./gradlew jacocoTestReport
```

## 📚 API 문서

애플리케이션 실행 후 다음 URL에서 API 문서를 확인할 수 있습니다:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🔐 인증 및 권한

### 사용자 유형
- **USER**: 사장님 (웹 관리자)
- **EMPLOYEE**: 직원 (모바일 앱)
- **MEMBER**: 회원 (모바일 앱)

### API 엔드포인트 구조
```
/api/v1/
├── auth/           # 인증 (공통)
├── web/            # 웹 관리자 전용
├── employee/       # 직원앱 전용
└── member/         # 회원앱 전용
```

## 📊 모니터링

### 헬스체크
- **URL**: http://localhost:8080/actuator/health
- **상태**: MySQL, Redis 연결 상태 확인

### 메트릭
- **URL**: http://localhost:8080/actuator/metrics
- **Prometheus**: http://localhost:8080/actuator/prometheus

## 🚀 배포

### Docker 이미지 빌드
```bash
docker build -t fitness-app:latest .
```

### CI/CD
GitHub Actions를 통한 자동 빌드 및 테스트:
- PR 생성 시 자동 테스트 실행
- main/develop 브랜치 push 시 Docker 이미지 빌드

## 🤝 개발 가이드

### 코딩 컨벤션
- **Java**: Google Java Style Guide
- **Commit**: Conventional Commits
- **Branch**: Git Flow 전략

### 개발 프로세스
1. Feature 브랜치 생성
2. 개발 및 테스트 코드 작성
3. Pull Request 생성
4. 코드 리뷰 후 Merge

### 모듈 간 통신
- **인터페이스 기반**: common.interfaces 패키지 활용
- **이벤트 기반**: Spring Application Events 활용

## 📝 주요 기능

### 웹 관리자 기능
- 대시보드 및 통계
- 회원 관리 (등록, 수정, 조회)
- 직원 관리 및 승인
- 상품 관리
- 락커룸 관리
- 일정 관리
- 커뮤니티 관리

### 직원앱 기능
- 담당 회원 관리
- 운동일지 작성
- 식단 피드백
- 개인 일정 관리
- 실적 조회

### 회원앱 기능
- 개인 정보 관리
- 운동일지 조회
- 식단 기록
- 체중 관리
- 레슨 일정 조회

## 🆘 문제 해결

### 자주 발생하는 문제

#### 1. MySQL 연결 실패
```bash
# MySQL 컨테이너 상태 확인
docker-compose ps mysql

# MySQL 로그 확인
docker-compose logs mysql
```

#### 2. Redis 연결 실패
```bash
# Redis 컨테이너 상태 확인
docker-compose ps redis

# Redis 연결 테스트
docker-compose exec redis redis-cli ping
```

#### 3. 빌드 실패
```bash
# Gradle 캐시 정리
./gradlew clean

# 의존성 새로고침
./gradlew --refresh-dependencies
```

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.

## 👥 팀

- **Backend Developer**: 개발자 A, 개발자 B
- **Architecture**: 시니어 개발자

---

💡 **Tip**: 개발 중 문제가 발생하면 GitHub Issues를 통해 문의해주세요.