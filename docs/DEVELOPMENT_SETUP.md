# 🏋️ 피트니스 관리 시스템 - 개발 환경 설정 가이드

## 📋 환경별 데이터베이스 구성

| 환경 | 데이터베이스 | 설정 파일 | 용도 |
|------|-------------|-----------|------|
| **Production** | AWS RDS MySQL | `application-prod.yml` | 실제 서비스 운영 |
| **Local** | 로컬 MySQL 서버 | `application-local.yml` | 로컬 개발 |
| **Test** | H2 메모리 DB | `application-test.yml` | 단위/통합 테스트 |

---

## 🚀 로컬 개발 환경 설정

### 1. 사전 요구사항
- **Java 17+** (현재 Java 23 사용)
- **MySQL 8.0+** 
- **Gradle 8.0+**

### 2. MySQL 설치 및 설정

#### macOS (Homebrew)
```bash
# MySQL 설치
brew install mysql

# MySQL 서비스 시작
brew services start mysql

# 초기 설정 (옵션)
mysql_secure_installation
```

#### Ubuntu/Debian
```bash
# MySQL 설치
sudo apt-get update
sudo apt-get install mysql-server

# MySQL 서비스 시작
sudo systemctl start mysql
sudo systemctl enable mysql

# 초기 설정
sudo mysql_secure_installation
```

#### Windows
MySQL 공식 사이트에서 MySQL Community Server 다운로드 및 설치

### 3. 개발 환경 실행

#### 자동 설정 (권장)
```bash
# 전체 환경 자동 설정
./scripts/setup-local.sh
```

#### 수동 설정
```bash
# 1. 데이터베이스 생성
mysql -u root -e "CREATE DATABASE IF NOT EXISTS fitness_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 2. 애플리케이션 실행
./gradlew bootRun --args='--spring.profiles.active=local'
```

---

## 🧪 테스트 실행

### 단위 테스트 (H2 메모리 DB 자동 사용)
```bash
./gradlew test
```

### 통합 테스트
```bash
./gradlew integrationTest
```

### 전체 빌드 + 테스트
```bash
./gradlew build
```

---

## 🌐 접속 정보

### 로컬 개발 환경
- **애플리케이션**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **MySQL**: localhost:3306
  - Database: `fitness_db`
  - Username: `root`
  - Password: (로컬 MySQL 설정에 따라)

### H2 테스트 DB (테스트 실행 시)
- **H2 Console**: http://localhost:8080/h2-console (테스트 프로필로 실행 시)
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (없음)

---

## 🔧 환경별 실행 방법

### 로컬 개발 (MySQL)
```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

### 테스트 환경 (H2)
```bash
./gradlew bootRun --args='--spring.profiles.active=test'
```

### 프로덕션 (환경변수 설정 필요)
```bash
export DATABASE_URL=jdbc:mysql://your-rds-endpoint:3306/fitness_db
export DATABASE_USERNAME=admin
export DATABASE_PASSWORD=your-password
export JWT_SECRET=your-production-secret

./gradlew bootRun --args='--spring.profiles.active=prod'
```

---

## 🐛 트러블슈팅

### MySQL 연결 오류
```bash
# MySQL 서비스 상태 확인
brew services list | grep mysql  # macOS
sudo systemctl status mysql      # Ubuntu

# MySQL 재시작
brew services restart mysql      # macOS
sudo systemctl restart mysql     # Ubuntu
```

### 권한 오류
```bash
# MySQL root 접속 확인
mysql -u root -p

# 데이터베이스 생성 권한 확인
SHOW GRANTS FOR 'root'@'localhost';
```

### 포트 충돌
- 기본 포트 8080이 사용 중인 경우:
```bash
./gradlew bootRun --args='--server.port=8081 --spring.profiles.active=local'
```

---

## 📚 다음 단계

1. **엔티티 관계 검증**: 도메인 모델 점검
2. **Security 설정**: JWT 인증 시스템 구현  
3. **기본 API 구현**: CRUD 작업 시작
4. **테스트 코드 작성**: 각 계층별 테스트 구현

---

## 🔗 관련 문서

- [프로젝트 요구사항](../CLAUDE.md)
- [데이터베이스 스키마](./database-schema.md)
- [API 문서](http://localhost:8080/swagger-ui.html) (로컬 실행 시)